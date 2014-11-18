package com.core.agents;

import java.io.IOException;
import java.io.Serializable;

import com.util.database.pojos.Lote;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


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
						//Liberar acesso web dos agentes humanos 
						
						
						
						
						
						
					}
					if(mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_A_VENDA)
							&& mensagem.getSender()==(new AID("Leiloeiro",AID.ISLOCALNAME)))
					{
						
						
						try
						{
							//Informa ao Grails qual o lote que esta sendo leiloado
							
							
							//Informa aos arrematantes qual lote esta sendo leiloado
							
							Lote lote= (Lote)mensagem.getContentObject();
							ACLMessage mensagemProposta= new ACLMessage(ACLMessage.CFP);
							
							mensagemProposta.setContentObject(lote);
							
							if(agentesArrematantes!=null)
							{
								for(DFAgentDescription agente: agentesArrematantes)
								{
									mensagemProposta.addReceiver(agente.getName());
									
								}
								
							}
							
							myAgent.send(mensagemProposta);
							
							 							
						}
						catch (IOException e) {
							
							e.printStackTrace();
						}
						catch (UnreadableException e2) 
						{
							
							e2.printStackTrace();
						}

						
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
					
					if(mensagem.getPerformative()==ACLMessage.PROPOSE)
					{
						ACLMessage propostaArrematanteVirtual= new ACLMessage(ACLMessage.PROPOSE);
						try 
						{
							
							
							propostaArrematanteVirtual.setContentObject((Serializable)mensagem.getContentObject());
							propostaArrematanteVirtual.setSender(new AID("Leiloeiro",AID.ISLOCALNAME));
							
							myAgent.send(propostaArrematanteVirtual);
							
							
						} catch (IOException e) 
						{

							e.printStackTrace();
						} catch (UnreadableException e) 
						{
							
							e.printStackTrace();
						}
						
					}
					

					
				}else block();
				
				
			}catch(FIPAException e)
			{
				e.printStackTrace();
			}
			
			
			
		}

		
		
	}

}
