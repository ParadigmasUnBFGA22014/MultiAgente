package com.core.agents;

import java.util.ArrayList;

import com.util.database.pojos.Lote;
import com.util.database.pojos.Objeto;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class Leiloeiro extends Agent{
	

	private int qtdLances=1;
	private Leiloeiro leiloeiro=this;
	private static final long serialVersionUID = 1L;
	
	protected void setup()
	{
		try
		{
			DFAgentDescription descricaoAgente= new DFAgentDescription();
			ServiceDescription servico = new ServiceDescription();
			servico.setName("leiloeiro");
			servico.setType("leilao");
			descricaoAgente.addServices(servico);
			
			DFService.register(this, descricaoAgente);
			
			
			addBehaviour(new OneShotBehaviour(this)
			{
				
				private static final long serialVersionUID = 1L;

				@Override
				public void action() 
				{
					try
					{
						ACLMessage iniciarLeilao= new ACLMessage(ACLMessage.INFORM);
						iniciarLeilao.addReceiver(new AID("Auxiliar",AID.ISLOCALNAME));
						iniciarLeilao.setConversationId(ConversationsAID.AUTORIZA_INICIO_LEILAO);
						
						myAgent.send(iniciarLeilao);
						
						System.out.println("Autorizei o leilao");
						
						addBehaviour(new Leiloar(lotes(),leiloeiro));
						
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
			});
			
	
			
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
						loteVendido.addReceiver(new AID("Auxiliar",AID.ISLOCALNAME));
						
						myAgent.send(loteVendido);
						
						if(lotes!=null &&lotes.size()>=1)
						{
							lote=lotes.get(0);
							lotes.remove(0);
							
							ACLMessage loteAvenda= new ACLMessage(ACLMessage.INFORM);
							loteAvenda.addReceiver(new AID("Auxiliar",AID.ISLOCALNAME));
							loteAvenda.setContentObject(lote);
							loteAvenda.setConversationId(ConversationsAID.LOTE_A_VENDA);
							
							myAgent.send(loteAvenda);
							
						}else
						{
							ACLMessage leilaoEncerrado= new ACLMessage(ACLMessage.INFORM);
							leilaoEncerrado.setConversationId(ConversationsAID.LEILAO_ENCERRADO);
							leilaoEncerrado.addReceiver(new AID("Auxiliar", AID.ISLOCALNAME));
							
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
