package com.core.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.util.database.pojos.Lote;


	public class Auxiliar  extends Agent{
	private Auxiliar auxiliar;
	private Map<String,Double> listaArrematantes;
	private DFAgentDescription[] agentesArrematantes;
	private boolean travaPropostas;
	
	protected void setup()
	{
		auxiliar=this;
		listaArrematantes= new HashMap<String, Double>();
		travaPropostas=false;
		
		try
		{
			DFAgentDescription descricaoAgente = new DFAgentDescription();
			descricaoAgente.setName(getAID());
			
			ServiceDescription descricaoServico= new ServiceDescription();
			descricaoServico.setName("auxiliar");
			descricaoServico.setType("leilao");
			
			descricaoAgente.addServices(descricaoServico);
	
			DFService.register(this, descricaoAgente);
			
			DFAgentDescription paginasAmarelas= new DFAgentDescription();
			ServiceDescription servico= new ServiceDescription();
			
			servico.setName("arrematante");
			servico.setType("leilao");
			
			paginasAmarelas.addServices(servico);
			
			agentesArrematantes= DFService.search(auxiliar, paginasAmarelas);
			
			if(agentesArrematantes!=null)
			{
				System.out.println("Temos "+agentesArrematantes.length+" Agentes virtuais que participaram do leilao");
			
				
				for(DFAgentDescription agente: agentesArrematantes)
				{
					System.out.println(agente.getName().getLocalName()+ " Vai participar do leilao.");
					listaArrematantes.put(agente.getName().getLocalName().toString(), 0.0);
						
				}
				
				System.out.println(" Lista com nomes de arrematantes cadastrada,  "+listaArrematantes.size()+ " pessoas participarao.");
			}
			
			
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
				ACLMessage mensagem=myAgent.receive();
				if(mensagem!=null)
				{
					if(mensagem.getSender().getLocalName().equalsIgnoreCase((new AID("Leiloeiro",AID.ISLOCALNAME)).getLocalName())
							&& mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.AUTORIZA_INICIO_LEILAO))
					{
						//Liberar acesso web dos agentes humanos 
						System.out.println("Leilao autorizado");
	
					}
					if(mensagem.getSender().getLocalName().equalsIgnoreCase((new AID("Leiloeiro",AID.ISLOCALNAME)).getLocalName())
							&& mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_A_VENDA))
					{
						try
						{
							System.out.println("Lote a venda 1234");
							//Informa aos arrematantes qual lote esta sendo leiloado
							System.out.println("é adki lance corrente: " + ((Lote)mensagem.getContentObject()).getLanceCorrente());
							Lote lote= (Lote)mensagem.getContentObject();
							ACLMessage mensagemProposta= new ACLMessage(ACLMessage.CFP);
							
							mensagemProposta.setContentObject(lote);
							mensagemProposta.setContent(mensagem.getContent());
							
							if(agentesArrematantes!=null)
							{
								for(DFAgentDescription agente: agentesArrematantes)
								{
									mensagemProposta.addReceiver(agente.getName());
									
								}
								
							}
							
							myAgent.send(mensagemProposta);
							travaPropostas=false;
							 							
						}
						catch (IOException e) {
							
							e.printStackTrace();
						}
						catch (UnreadableException e2) 
						{
							
							e2.printStackTrace();
						}

						
					}
					if(mensagem.getSender().getLocalName().equalsIgnoreCase((new AID("Leiloeiro",AID.ISLOCALNAME)).getLocalName())
							&& mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LOTE_VENDIDO))
					{
						try
						{
							System.out.println("Lote Vendido ");
							System.out.println("Ganhador"+mensagem.getContent());
							
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}
					
					if(mensagem.getSender().getLocalName().equalsIgnoreCase((new AID("Leiloeiro",AID.ISLOCALNAME)).getLocalName())
							&& mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.LEILAO_ENCERRADO))
					{
						
					}
					
					if(mensagem.getPerformative()==ACLMessage.PROPOSE)
					{
						ACLMessage propostaArrematanteVirtual= new ACLMessage(ACLMessage.PROPOSE);
						propostaArrematanteVirtual.setPerformative(ACLMessage.PROPOSE);
						try 
						{
							
							if(!travaPropostas)
							{
								propostaArrematanteVirtual.setContent(mensagem.getSender().getLocalName());
								propostaArrematanteVirtual.addReceiver(new AID("Leiloeiro",AID.ISLOCALNAME));
								
								myAgent.send(propostaArrematanteVirtual);
								travaPropostas=true;
							}
							
						} catch (Exception e) 
						{
							e.printStackTrace();
						} 						
					}
					

					
				}else block();
				
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			
		}

		
		
	}

}
