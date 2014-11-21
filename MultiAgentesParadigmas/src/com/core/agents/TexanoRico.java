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

public class TexanoRico extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double valorCarteira=0.0;
	private TexanoRico texanoRico;

	protected void setup()
	{
		try
		{
			texanoRico=this;
			DFAgentDescription descricaoAgente= new DFAgentDescription();
			ServiceDescription servico= new ServiceDescription();
			
			servico.setName("arremantante");
			servico.setType("leilao");
			
			descricaoAgente.setName(this.getAID());
			descricaoAgente.addServices(servico);
			
			DFService.register(texanoRico, descricaoAgente);
			
			Object[] argumentos= getArguments();
			if(argumentos!=null && argumentos.length>0)
			{
				this.valorCarteira= Double.parseDouble((String)argumentos[0]);
				
			}
			
						
		  System.out.println("Oi meu nome eh "+this.getLocalName()+ " e tenho "+this.valorCarteira+" na carteira para gastar");
			
		  addBehaviour(new ofertarLance());

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
		private Lote loteComprado=null;
		private static final long serialVersionUID = 1L;

		@Override
		public void action() 
		{
			
			try
			{
				ACLMessage mensagem= myAgent.receive();
				ACLMessage resposta=null;
				if(mensagem!=null)
				{
					if(mensagem.getPerformative()==ACLMessage.CFP)
					{
						
						
						if(ofertar((Lote) mensagem.getContentObject()))
						{
							
							resposta = new ACLMessage(ACLMessage.PROPOSE);
							resposta.addReceiver(mensagem.getSender());
							
							myAgent.send(resposta);
						}
					}
					if(mensagem.getPerformative()==ACLMessage.INFORM && mensagem.getConversationId().equalsIgnoreCase(ConversationsAID.PARABENS))
					{
						this.loteComprado=(Lote)mensagem.getContentObject();
						texanoRico.valorCarteira-=this.loteComprado.getLanceCorrente();
						
						System.out.println(texanoRico.getLocalName()+": "+" Uhuuu ganhei!");
						System.out.println(texanoRico.getLocalName()+": "+" Agora tenho um/uma "+loteComprado.getObjeto().getNome());
						System.out.println(texanoRico.getLocalName()+": "+" Ainda tenho R$ "+texanoRico.valorCarteira+ "pagar gastar!");
						
					}
					
				}else block();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		private boolean ofertar(Lote lote)
		{
			//Criterios de escolha 
			if(lote.getLanceCorrente()<texanoRico.valorCarteira)
			{
				return true;
			}
			else
			return false;
		}
		
	}

}
