package com.core.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import com.util.database.pojos.Lote;


public class Leiloeiro extends Agent{
	

	private int qtdLances=0;
	private Leiloeiro leiloeiro=this;
	private static final long serialVersionUID = 1L;
	private AID agenteAuxiliarAID=null;
	private DFAgentDescription[] agenteAuxiliar=null;
	public ArrayList<Lote>lotes=lotes();
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
			
			DFAgentDescription paginasAmarelas= new DFAgentDescription();
			ServiceDescription servicoProcurado= new ServiceDescription();
			
			servicoProcurado.setName("auxiliar");
			servicoProcurado.setType("leilao");
			
			paginasAmarelas.addServices(servicoProcurado);
			
			agenteAuxiliar= DFService.search(leiloeiro, paginasAmarelas);
			
			if(agenteAuxiliar!=null)
			{
				agenteAuxiliarAID=agenteAuxiliar[0].getName();
				System.out.println("Achei um auxiliar.. vamos iniciar o leilao.");
				
				addBehaviour(new IniciarLeilao(this.lotes, this));
			}else
			{
				System.out.println("Não tenho um auxiliar para me ajudar, nao posso iniciar o leilao!");
			}
			
			
			
			
	
			
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
	
	private class IniciarLeilao extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;
		protected ArrayList<Lote> lotes;
		
		public IniciarLeilao(ArrayList<Lote>lotes, Agent myAgent) {
			super(myAgent);
			this.lotes = lotes;
		}

		@Override
		public void action() 
		{
			try
			{
				ACLMessage iniciarLeilao= new ACLMessage(ACLMessage.INFORM);
				iniciarLeilao.addReceiver(agenteAuxiliarAID);
				iniciarLeilao.setConversationId(ConversationsAID.AUTORIZA_INICIO_LEILAO);
				
				myAgent.send(iniciarLeilao);
				System.out.println("Lote Corrente1: "+this.lotes.get(0).getNumeroLote());
				
				ACLMessage loteAvenda= new ACLMessage(ACLMessage.INFORM);
				loteAvenda.addReceiver(agenteAuxiliarAID);
				loteAvenda.setContentObject(this.lotes.get(0));
				loteAvenda.setContent("");
				loteAvenda.setConversationId(ConversationsAID.LOTE_A_VENDA);
				
				myAgent.send(loteAvenda);
				
				System.out.println("Autorizei o leilao");
				
				addBehaviour(new Leiloar(this.lotes,leiloeiro));
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private class Leiloar extends CyclicBehaviour
	{
		private static final long serialVersionUID = 1L;
		private long startLeilaoLote=System.currentTimeMillis();
		private String nomeGanhador="";
		protected ArrayList<Lote> lotes;
		
		
		public Leiloar(ArrayList<Lote>lotes,Agent agent)
		{
			super(agent);
			try
			{
				this.lotes=lotes;
				
				System.out.println("Lote Corrente2: "+this.lotes.get(0).getNumeroLote());
				
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
						lotes.get(0).setLanceCorrente(leiloeiro.qtdLances * lotes.get(0).getValorIncremento() + lotes.get(0).getLanceInicialValor());
						leiloeiro.qtdLances++;
						nomeGanhador=mensagens.getContent();
						startLeilaoLote=System.currentTimeMillis();
						
						System.out.println("Quantidade de lances: "+leiloeiro.qtdLances);
						System.out.println("Lance corrente: "+lotes.get(0).getLanceCorrente());
						System.out.println("Ganhador atual: "+nomeGanhador);
						
						ACLMessage loteAvenda= new ACLMessage(ACLMessage.INFORM);
						loteAvenda.addReceiver(agenteAuxiliarAID);
						loteAvenda.setContentObject(lotes.get(0));
						loteAvenda.setContent(nomeGanhador);
						loteAvenda.setConversationId(ConversationsAID.LOTE_A_VENDA);
						
						myAgent.send(loteAvenda);
						
					}
					if((startLeilaoLote-System.currentTimeMillis())>10000)
					{
						ACLMessage loteVendido= new ACLMessage(ACLMessage.INFORM);
						
						loteVendido.setConversationId(ConversationsAID.LOTE_VENDIDO);
						loteVendido.setContent(nomeGanhador);
						loteVendido.setContentObject(lotes.get(0));
						loteVendido.addReceiver(agenteAuxiliarAID);
						
						myAgent.send(loteVendido);
						
						System.out.println("Lote Vendido: "+this.lotes.get(0).getNumeroLote());
						System.out.println("Ganhador: "+nomeGanhador);
						this.lotes.remove(0);
						
						if(this.lotes!=null && lotes.size()>=1)
						{
							
							System.out.println("Lote Corrente3: "+this.lotes.get(0).getNumeroLote());
							
							ACLMessage loteAvenda= new ACLMessage(ACLMessage.INFORM);
							loteAvenda.addReceiver(agenteAuxiliarAID);
							loteAvenda.setContentObject(lotes.get(0));
							loteAvenda.setContent(nomeGanhador);
							loteAvenda.setConversationId(ConversationsAID.LOTE_A_VENDA);
							
							myAgent.send(loteAvenda);
							
						}else
						{
							ACLMessage leilaoEncerrado= new ACLMessage(ACLMessage.INFORM);
							leilaoEncerrado.setConversationId(ConversationsAID.LEILAO_ENCERRADO);
							leilaoEncerrado.addReceiver(agenteAuxiliarAID);
							
							myAgent.send(leilaoEncerrado);
							myAgent.removeBehaviour(this);
							
							System.out.println("O leilão acabou!!");
							
						}
						
					}
				}else {
					block();
				}
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
						
		}
		
	}
	
	private ArrayList<Lote> lotes()
	{
		ArrayList<Lote>lotes=new ArrayList<Lote>();
		Lote lote=new Lote();
		
		lote.setLanceInicialValor(1000);
		lote.setValorIncremento(100);
		lote.setNumeroLote(1);
		lote.setLanceCorrente(1000);
		
		lotes.add(lote);
		
		Lote lote2=new Lote();
		lote2.setLanceInicialValor(5000);
		lote2.setValorIncremento(1000);
		lote2.setNumeroLote(2);
		lote2.setLanceCorrente(5000);
		
		lotes.add(lote2);
		
		Lote lote3=new Lote();
		lote3.setLanceInicialValor(5000);
		lote3.setValorIncremento(1000);
		lote3.setNumeroLote(3);
		lote3.setLanceCorrente(5000);
		
		lotes.add(lote3);
		
		Lote lote4=new Lote();
		lote4.setLanceInicialValor(5000);
		lote4.setValorIncremento(1000);
		lote4.setNumeroLote(4);
		lote4.setLanceCorrente(5000);
		
		lotes.add(lote4);
		
		Lote lote5=new Lote();
		lote5.setLanceInicialValor(5000);
		lote5.setValorIncremento(1000);
		lote5.setNumeroLote(5);
		lote5.setLanceCorrente(5000);
		
		lotes.add(lote5);
		
		return lotes;
	}
	

}
