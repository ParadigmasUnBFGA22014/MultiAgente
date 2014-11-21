package com.core.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import com.util.Lote;
import com.util.Objeto;


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
			
			System.out.println(  "Ol‡ meu nome Ž "+this.getLocalName()+ " e serei o leiloeiro de hoje!");
			
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
	
	
	private class Leiloar extends Behaviour
	{
		private long inicioLeilao=0;
		private Lote loteCorrente=null;
		private ArrayList<Lote> listaDeLotes=null;
		private boolean travaPedidoLance=false;
		private boolean fimLeilao=false;
		private AID ganhadorAID=null;
		private String ganhadorNome=null;
		
		
		
		@Override
		public void onStart()
		{
			this.inicioLeilao=System.currentTimeMillis();
			
			this.listaDeLotes=leiloeiro.lotes();
			this.loteCorrente=this.listaDeLotes.get(this.listaDeLotes.size()-1);
			this.listaDeLotes.remove(this.listaDeLotes.size()-1);
			
			System.out.println(leiloeiro.getLocalName() +": "+"Muito bom, temos "+this.listaDeLotes.size()+" Lotes para leiloar");
			System.out.println(leiloeiro.getLocalName() +": "+"Primeiro Lote pronto para leilao");
			System.out.println(leiloeiro.getLocalName() +": "+"Vamos leiloar-> "+loteCorrente.getObjeto().getNome());
			
		}
		@Override
		public void action() 
		{
			if(this.listaDeLotes!=null && this.listaDeLotes.size()>0)
			{
				try
				{
					ACLMessage pedidoDeLance=null;
					ACLMessage lance=null;
					ACLMessage avisoGanhador=null;
					lance=myAgent.receive();
					
					if(travaPedidoLance)
					{
						
						if(lance!=null)
						{
							if(lance.getPerformative()==ACLMessage.PROPOSE)
							{
								
								this.ganhadorAID=lance.getSender();
								this.ganhadorNome=lance.getSender().getLocalName();
								
								System.out.println(leiloeiro.getLocalName() +": "+" Recebi um lance do "+this.ganhadorNome);
								
								this.loteCorrente.setLanceCorrente(this.loteCorrente.getLanceCorrente()+this.loteCorrente.getValorIncremento());
								travaPedidoLance=false;
								
							}
							
						}
						
						
						
						if(System.currentTimeMillis()-this.inicioLeilao>5000)
						{
							
							if(this.listaDeLotes.size()==0) this.fimLeilao=true;else this.fimLeilao=false;
							
							if(this.ganhadorAID!=null)
							{
								System.out.println(leiloeiro.getLocalName() +": "+"PARABENS "+ganhadorNome+" voce ganhou!");
								
								avisoGanhador=new ACLMessage(ACLMessage.INFORM);
								avisoGanhador.setConversationId(ConversationsAID.PARABENS);
								avisoGanhador.setContentObject(this.loteCorrente);
								avisoGanhador.addReceiver(ganhadorAID);
								
								myAgent.send(avisoGanhador);
								
								//Pega proximo lote 
								//Codigo duplicado
								this.ganhadorAID=null;
								this.ganhadorNome=null;
								this.loteCorrente=this.listaDeLotes.get(this.listaDeLotes.size()-1);
								this.listaDeLotes.remove(this.listaDeLotes.size()-1);
								
								System.out.println(leiloeiro.getLocalName() +": "+"Ok, vamos ao proximo Lote");
								System.out.println(leiloeiro.getLocalName() +": "+"vamos leiloar-> "+this.loteCorrente.getObjeto().getNome());
								
								this.inicioLeilao=System.currentTimeMillis();
								travaPedidoLance=false;
								//Fim codigo duplicado
								
							}else
							{
								if(!this.fimLeilao)
								{
									System.out.println(leiloeiro.getLocalName() +": "+"Nenhuma oferta pelo(a) "+this.loteCorrente.getObjeto().getNome());
									System.out.println(leiloeiro.getLocalName() +": "+"Ok, vamos ao proximo lote");
									
									//Pega proximo lote 
									//Codigo duplicado
									this.ganhadorAID=null;
									this.ganhadorNome=null;
									this.loteCorrente=this.listaDeLotes.get(this.listaDeLotes.size()-1);
									this.listaDeLotes.remove(this.listaDeLotes.size()-1);
									
									System.out.println(leiloeiro.getLocalName() +": "+"Ok, vamos ao proximo Lote");
									System.out.println(leiloeiro.getLocalName() +": "+"vamos leiloar-> "+this.loteCorrente.getObjeto().getNome());
									
									this.inicioLeilao=System.currentTimeMillis();
									travaPedidoLance=false;
									//Fim codigo duplicado
								}
								
							}
							
	
							
						}
						
					}else
					{
						
						for(DFAgentDescription arrematantes:leiloeiro.agenteArremantantes)
						{
							pedidoDeLance= new ACLMessage(ACLMessage.CFP);
							pedidoDeLance.setContentObject(this.loteCorrente);
							pedidoDeLance.addReceiver(arrematantes.getName());
							
							myAgent.send(pedidoDeLance);

							travaPedidoLance=true;
						}
						System.out.println(leiloeiro.getLocalName() +": R$ "+this.loteCorrente.getLanceCorrente()+" pelo/a "+this.loteCorrente.getObjeto().getNome());
						
					}
					
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			
			
			
		}

		@Override
		public boolean done() 
		{
			
			if(this.fimLeilao)
				System.out.println(leiloeiro.getLocalName() +": "+" Leil‹o incerrado! AtŽ mais pessoal ");
				
				return this.fimLeilao;
			
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
						System.out.println(leiloeiro.getLocalName() +": "+"Muito bom temos "+leiloeiro.agenteArremantantes.length+ " participantes.\n");
						
						for(DFAgentDescription arremantantes: leiloeiro.agenteArremantantes)
						{
							System.out.println(leiloeiro.getLocalName() +": "+"Ol‡ "+arremantantes.getName().getLocalName()+", bem vindo!");
						}
						
						addBehaviour(new Leiloar());
						
						
					}else 
					{
						System.out.println(leiloeiro.getLocalName() +": "+"Ainda n‹o tem arrematantes no leilao, nao ser‡ possivel iniciar agora ");
						System.out.println(leiloeiro.getLocalName() +": "+"Vou aguardar mais um pouco e tentar novamente.");
						
						addBehaviour(new WakerBehaviour(leiloeiro,5000) 
						{
							public void onWake()
							{
								leiloeiro.prepararLeilao();
							}
						});
						
					}
					
					
				}catch(FIPAException e)
				{
					e.printStackTrace();
				}

			}
		});//fim comportamento
		
		
		addBehaviour(prepararLeilao);


	}
	
	private ArrayList<Lote> lotes()
	{
		
		ArrayList<Lote>lotes=new ArrayList<Lote>();
		Lote lote=null;
		Objeto objeto=null;
		
		//Lote 001
		lote= new Lote();
		objeto= new Objeto(01, "Bicicleta");
		
		lote.setLanceInicialValor(100);
		lote.setValorIncremento(50);
		lote.setNumeroLote(001);
		lote.setLanceCorrente(100);
		lote.setObjeto(objeto);
		
		lotes.add(lote);
		
		//Lote 002
		lote= new Lote();
		objeto= new Objeto(02, "Computador");
		
		lote.setLanceInicialValor(300);
		lote.setValorIncremento(50);
		lote.setNumeroLote(002);
		lote.setLanceCorrente(300);
		lote.setObjeto(objeto);
		
		lotes.add(lote);
		
		
		//Lote 003
		lote= new Lote();
		objeto= new Objeto(03, "Cachorro");
		
		lote.setLanceInicialValor(100);
		lote.setValorIncremento(50);
		lote.setNumeroLote(003);
		lote.setLanceCorrente(100);
		lote.setObjeto(objeto);
		
		lotes.add(lote);
		

		
		//Lote 004
		lote= new Lote();
		objeto= new Objeto(04, "BarrilPetroleo");
		
		lote.setLanceInicialValor(200);
		lote.setValorIncremento(100);
		lote.setNumeroLote(004);
		lote.setLanceCorrente(200);
		lote.setObjeto(objeto);
		
		lotes.add(lote);
		
		
		
		
		return lotes;
	}
	

}
