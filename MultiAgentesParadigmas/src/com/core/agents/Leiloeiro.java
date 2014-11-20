package com.core.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import com.util.database.pojos.Lote;


public class Leiloeiro extends Agent{
	

	private int qtdLances=1;
	private Leiloeiro leiloeiro=null;
	private static final long serialVersionUID = 1L;
	private AID agenteAuxiliarAID=null;
	private DFAgentDescription[] agenteArremantantes=null;
	
	protected void setup()
	{
		try
		{
			leiloeiro=this;
			//Registrando nas paginas amarelas
			DFAgentDescription descricaoAgente= new DFAgentDescription();
			ServiceDescription servico = new ServiceDescription();
			servico.setName("leiloeiro");
			servico.setType("leilao");
			descricaoAgente.addServices(servico);
			
			DFService.register(leiloeiro, descricaoAgente);		
			
			System.out.println("Ol‡ meu nome Ž "+this.getLocalName()+ " e serei o leiloeiro de hoje!");
			
			leiloeiro.prepararLeilao();			

			
		}catch(FIPAException e)
		{
			e.printStackTrace();
		}
	}
	
	
	protected void takeDown()
	{
		try
		{
			DFService.deregister(this);
			
		}catch(FIPAException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private class Leiloar extends CyclicBehaviour
	{

		private static final long serialVersionUID = 1L;
		private Lote lote=null;
		private long startLeilaoLote=System.currentTimeMillis();
		private String nomeGanhador=null;
		private ArrayList<Lote>lotes=null;
		
		public Leiloar(ArrayList<Lote>lotes,Agent agent)
		{
			super(agent);
			try
			{
				this.lotes=lotes;
				this.lote=lotes.get(0);
				this.lotes.remove(0);
				
			}catch (Exception e)
			{
				e.printStackTrace();
			}
	
		}
		
		
		@Override
		public void action()
		{
			try
			{
				ACLMessage mensagens=myAgent.receive();
				if(mensagens!=null)
				{
					if(mensagens.getPerformative()==ACLMessage.PROPOSE)
					{
						
						lote.setLanceCorrente(lote.getLanceCorrente()*leiloeiro.qtdLances);
						nomeGanhador=mensagens.getContent();
						leiloeiro.qtdLances++;
						
					}
					if((startLeilaoLote-System.currentTimeMillis())>10000)
					{
						ACLMessage loteVendido= new ACLMessage(ACLMessage.INFORM);
						
						loteVendido.setConversationId(ConversationsAID.LOTE_VENDIDO);
						loteVendido.setContent(nomeGanhador);
						loteVendido.setContentObject(lote);
						loteVendido.addReceiver(agenteAuxiliarAID);
						
						myAgent.send(loteVendido);
						
						if(lotes!=null &&lotes.size()>=1)
						{
							lote=lotes.get(0);
							lotes.remove(0);
							
							ACLMessage loteAvenda= new ACLMessage(ACLMessage.INFORM);
							loteAvenda.addReceiver(agenteAuxiliarAID);
							loteAvenda.setContentObject(lote);
							loteAvenda.setConversationId(ConversationsAID.LOTE_A_VENDA);
							
							myAgent.send(loteAvenda);
							
						}else
						{
							ACLMessage leilaoEncerrado= new ACLMessage(ACLMessage.INFORM);
							leilaoEncerrado.setConversationId(ConversationsAID.LEILAO_ENCERRADO);
							leilaoEncerrado.addReceiver(agenteAuxiliarAID);
							
							myAgent.send(leilaoEncerrado);
							myAgent.removeBehaviour(this);
							
						}
						
					}
				}else block();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
						
		}
		
	}
	
	private void prepararLeilao()
	{
		 SequentialBehaviour prepararLeilao= new SequentialBehaviour(leiloeiro);
		
		//Procurar agentes arrematantes 
		prepararLeilao.addSubBehaviour(new OneShotBehaviour(leiloeiro) 
		{
			
			@Override
			public void action() 
			{
				
				//Buscando agentes arremantantes nas paginas amarelas
				DFAgentDescription paginasAmarelas= new DFAgentDescription();
				ServiceDescription servicoProcurado= new ServiceDescription();
				
				try
				{
					servicoProcurado.setName("arremantante");
					servicoProcurado.setType("leilao");
					
					paginasAmarelas.addServices(servicoProcurado);
					
					leiloeiro.agenteArremantantes= DFService.search(leiloeiro, paginasAmarelas);
					
					
					
					if(leiloeiro.agenteArremantantes!=null  && leiloeiro.agenteArremantantes.length>0)
					{
						System.out.println("Muito bom temos "+leiloeiro.agenteArremantantes.length+ " participantes.\n");
						
						for(DFAgentDescription arremantantes: leiloeiro.agenteArremantantes)
						{
							System.out.println("Ol‡ "+arremantantes.getName().getLocalName()+", bem vindo!");
						}
						
						
					}else 
					{
						System.out.println("Ainda n‹o tem arrematantes no leilao, nao ser‡ possivel iniciar agora ");
					}
					
				
					
					
				}catch(FIPAException e)
				{
					e.printStackTrace();
				}
				
					
				
			}
		});
		
		addBehaviour(prepararLeilao);
	
					
		

	}
	
	private ArrayList<Lote> lotes()
	{
		ArrayList<Lote>lotes=new ArrayList<Lote>();
		Lote lote=null;
		
		lote= new Lote();
		
		lote.setLanceInicialValor(1000);
		lote.setValorIncremento(100);
		lote.setNumeroLote(001);
		lote.setLanceCorrente(1000);
		
		lotes.add(lote);
		
		lote.setLanceInicialValor(5000);
		lote.setValorIncremento(1000);
		lote.setNumeroLote(002);
		lote.setLanceCorrente(5000);
		
		lotes.add(lote);
		
		return lotes;
	}
	

}
