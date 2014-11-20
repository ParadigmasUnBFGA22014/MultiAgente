package com.core.agents;

import com.util.database.pojos.Lote;

import examples.yellowPages.DFRegisterAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Arrematante extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double valorCarteira=0.0;
	private Arrematante arrematante;

	protected void setup()
	{
		try
		{
			arrematante=this;
			DFAgentDescription descricaoAgente= new DFAgentDescription();
			ServiceDescription servico= new ServiceDescription();
			
			servico.setName("arrematante");
			servico.setType("leilao");
			
			descricaoAgente.setName(this.getAID());
			descricaoAgente.addServices(servico);
			
			DFService.register(this, descricaoAgente);
			
			Object[] argumentos= getArguments();
			if(argumentos!=null && argumentos.length>0)
			{
				this.valorCarteira= Double.parseDouble((String)argumentos[0]);
				
			}
			
			addBehaviour(new ofertarLance());
			
		  System.out.println("Oi meu nome eh "+this.getLocalName()+ " e tenho "+this.valorCarteira+" na carteira para gastar");
			
			
		}catch(FIPAException e )
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
	
	private class ofertarLance extends CyclicBehaviour
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() 
		{
			
			try
			{
				ACLMessage mensagem= myAgent.receive();
				ACLMessage resposta;
				if(mensagem!=null)
				{
					if(mensagem.getPerformative()==ACLMessage.CFP)
					{
						if(ofertar((Lote) mensagem.getContentObject(), mensagem.getContent()))
						{
							resposta = mensagem.createReply();
							resposta.setPerformative(ACLMessage.PROPOSE);
							
							myAgent.send(resposta);
							
							System.out.println("O arremantante "+myAgent.getAID().getLocalName()+" deu um lance!");
						}
					}
					
				}else block();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		private boolean ofertar(Lote lote, String ganhadorAtual)
		{
			//Criterios de escolha 
			if(lote.getLanceCorrente()<arrematante.valorCarteira && ganhadorAtual != this.getAgent().getLocalName())
			{
				return true;
			}
			else
			{
				System.out.println("O arremantante "+myAgent.getAID().getLocalName()+" não possui mais dinheiro para esse lote!");
				return false;
			}
		}
		
	}

}
