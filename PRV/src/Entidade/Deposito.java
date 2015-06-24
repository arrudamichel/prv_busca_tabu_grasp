package Entidade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import IO.ManipulaDados;

public class Deposito {
	
	private float coordenadaX;
	private float coordenadaY;
	private Veiculo veiculo;
	ArrayList<Integer> listaColunas =  new ArrayList<Integer>();	
	private ArrayList<Rota> listaRota = new ArrayList<>();
	private HashMap<Integer, Cliente> listaCliente = new HashMap<>(); 
	private float[][] matrizCustos;
	private HashMap<Integer, Float> listaDemanda = new HashMap<>();

	public Deposito() {
		// TODO Auto-generated constructor stub
	}

	public float getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(float coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public float getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(float coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public ArrayList<Rota> getListaRota() {
		return listaRota;
	}

	public void setListaRota(ArrayList<Rota> listaRota) {
		this.listaRota = listaRota;
	}

	public float[][] getMatrizCustos() {
		return matrizCustos;
	}

	public void setMatrizCustos(float[][] matrizCustos) {
		this.matrizCustos = matrizCustos;
	}

	public HashMap<Integer, Cliente> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(HashMap<Integer, Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public void criaCliente(String identificador, String coordenadaX, String coordenadaY,
			String demanda, String tempoInicio, String tempoFim, String string7) {
		
		Cliente cliente = new Cliente(Integer.parseInt(identificador), Float.parseFloat(coordenadaX), Float.parseFloat(coordenadaY), 
										Float.parseFloat(demanda), Float.parseFloat(tempoInicio), Float.parseFloat(tempoFim));
		
		this.listaCliente.put(Integer.parseInt(identificador), cliente);
	}

	public void criaVeiculo(String capacidade) {
		this.veiculo = new Veiculo();
		this.veiculo.setCapacidade(Integer.parseInt(capacidade));
	}
	
	public void populaMatrizCusto() {
		int tamanho = this.listaCliente.size();
		this.matrizCustos = new float[tamanho][tamanho];
		
		for(int i = 1; i <= this.matrizCustos.length; i++){
			for(int j = 1; j <= this.matrizCustos.length; j++){
				this.matrizCustos[i-1][j-1] = calculaDistancia(this.listaCliente.get(i).getCoordenadaX(), this.listaCliente.get(j).getCoordenadaX(), 
															   this.listaCliente.get(i).getCoordenadaY(), this.listaCliente.get(j).getCoordenadaY());
			}
		}
		
		ManipulaDados manipula = new ManipulaDados();
		manipula.escreveArquivo(this.matrizCustos, "matrizCusto.txt");
		
	}
	
	public float calculaDistancia(float x1, float x2, float y1, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
	public void populaListaDemanda() {
		for (int i=1; i <= this.listaCliente.size(); i++) {
			this.listaDemanda.put(this.listaCliente.get(i).getIdentificador(), this.listaCliente.get(i).getDemanda());
		}
	}
	
	public void criaSolucaoInicial() {
		int tamanhoLista = 3;
		int linhaAux = 0;
		HashMap<Integer, Cliente> listaCandidatos = this.listaCliente;
		
		Rota rota = new Rota();
		int linhaHist = 1;
		while(listaCandidatos.size() != 1){
			
			linhaHist = linhaAux;
			for(int colunaAux = 0; colunaAux < this.matrizCustos[linhaAux].length; colunaAux++) {
				
				//Verifica se n�o � o custo dele para ele
				//Verifica se pode ser inserido na lista de colunas
				//Verifica se n�o ultrapassa a capacidade do ve�culo)
								
				if(listaCandidatos.get(colunaAux) != null){
					if((linhaAux != colunaAux) && 
							(verificaCusto(listaColunas, colunaAux, linhaAux, tamanhoLista) && 
							(verificaCapacidade(rota.getCustoTotal(), 
									this.veiculo.getCapacidade(), 
									listaCandidatos.get(colunaAux).getDemanda())))){
						
						colocaListaColunas(listaColunas, colunaAux, linhaAux, tamanhoLista);
					}
				}												
			}
			
			//Insere cliente na rota			
			if(listaColunas.size() != 0){
				
				int coluna = escolheItemRota(listaColunas);
				
				rota.setItemListaCliente(listaCandidatos.get(listaColunas.get(coluna)));
				rota.setCustoTotal(rota.getCustoTotal() + listaColunas.get(coluna));
				linhaAux = listaColunas.get(coluna);
				
				listaCandidatos.remove(listaColunas.get(coluna));
				
				listaColunas.clear();
	
			} else {
								
				System.out.print("Rota - ");
				for (Cliente cliente : rota.getListaCliente()) {
					System.out.print(cliente.getIdentificador() + ";");
				}
				System.out.println("");
				linhaAux = 0;
				rota = new Rota();
			}
			
			
				
				/*try {
					File arquivo = new File("rota.txt");
					
					if (!arquivo.exists()) {			
						arquivo.createNewFile();
					}
					
					FileWriter arq = new FileWriter(arquivo, true); 
					PrintWriter gravarArq = new PrintWriter(arq); 

					for(int i=0; i<rota.getListaCliente().size(); i++) {
						gravarArq.printf(rota.getListaCliente().get(i).getIdentificador() + " ");
								
						
					}
					
					arq.close();
				} catch (IOException e) { 
					e.printStackTrace();
				}*/
			
			//Limpa listas			
		}
		
		
	}
	
	public int escolheItemRota(ArrayList<Integer> listaColunas) {
		
		if(listaColunas.size() == 1)
			return 0;
		
		Random gerador = new Random();
		
        int numero = gerador.nextInt(listaColunas.size() - 1);
        return numero;
	}
	
	public boolean verificaVazio(HashMap<Integer, Cliente> listaCandidatos) {
		if(listaCandidatos.size() == 1 && listaCandidatos.get(0) == null)
			return true;
		
		return false;
	}

	public boolean verificaCusto(ArrayList<Integer> listaColunas, int coluna, int linha, int tamanhoLista) {
		
		if(listaColunas.size() < tamanhoLista){
			return true;
		}
		
		for (Integer cliente : listaColunas) {
			if(matrizCustos[linha][coluna] < matrizCustos[linha][cliente]){
				return true;
			}
		}
		
		return false;
	}
	
	public void colocaListaColunas(ArrayList<Integer> listaColunas, int coluna, int linha, int tamanhoLista) {
		
		if(listaColunas.size() < tamanhoLista){		
			listaColunas.add(coluna);		
		} else {
			for(int i=0; i < listaColunas.size(); i++){			
				if(this.matrizCustos[linha][coluna] < this.matrizCustos[linha][listaColunas.get(i)]){
					listaColunas.remove(i);
					listaColunas.add(coluna);
				}
			}
		}
	}
	
	public boolean verificaCapacidade(float custoAtual, int capacidade, float demanda) {
		if((custoAtual + demanda) < capacidade)
			return true;
		else
			return false;
	}
}