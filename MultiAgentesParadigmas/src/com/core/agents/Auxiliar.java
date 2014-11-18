package com.core.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class Auxiliar  extends Agent{
	private Auxiliar auxiliar;
	protected void setup()
	{
		auxiliar=this;
		try
		{
			DFAgentDescription descricaoAgente = new DFAgentDescription();
			descricaoAgente.setName(getAID());
			DFService.register(this, descricaoAgente);
			
			
			//Iniciar auxilio ao leiloeiro
			
			addBehaviour(new AuxiliarLeilao());
			
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
	
	private class AuxiliarLeilao extends CyclicBehaviour
	{

		@Override
		public void action()
		{
			
			
			try
			{
				
				
				DFAgentDescription paginasAmarelas= new DFAgentDescription();
				ServiceDescription servico= new ServiceDescription();
				
				servico.setName("arrematante");
				servico.setType("leilao");
				
				paginasAmarelas.addServices(servico);
				
				DFAgentDescription[] agentesArrematantes= DFService.search(auxiliar, paginasAmarelas);
				
				if(agentesArrematantes!=null)
				{
					System.out.println("Temos "+agentesArrematantes.length+" Agentes virtuais que participaram do leil‹o");
				
					
					for(DFAgentDescription agente: agentesArrematantes)
					{
						System.out.println(agente.getName()+ " Vai participar do leilao.");
						
						
					}
				}
			
				
				ACLMessage mensagem=myAgent.receive();
				if(mensagem!=null)
				{
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.AUTORIZA_INICIO_LEILAO) 
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
					}
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_A_VENDA)
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
					}
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_VENDIDO)
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
					}
					
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_VENDIDO)
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
					}
					
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LEILAO_ENCERRADO)
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
					}

					
				}else block();
				
				
			}catch(FIPAException e)
			{
				e.printStackTrace();
			}
			
			
			
		}

		
		
	}

}
