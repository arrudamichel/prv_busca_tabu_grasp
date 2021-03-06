import java.util.ArrayList;

import Entidade.Cliente;
import Entidade.Deposito;
import Entidade.Rota;
import IO.ManipulaDados;


public class Main {

	public static void main(String[] args) {

			ManipulaDados entra = new ManipulaDados();
			ArrayList<String> registros = entra.lerArquivo();
			int linhaC = 0;
			Deposito deposito = new Deposito();
			
			for (String registro : registros) {
				String linhaArray[] = registro.split(";");
				
				if(linhaC == 0){
					deposito.criaVeiculo(linhaArray[0]);
				} else if (linhaC == 1){
					deposito.setCoordenadaX(Float.parseFloat(linhaArray[1]));
					deposito.setCoordenadaY(Float.parseFloat(linhaArray[2]));
					
					//deposito.criaCliente(linhaArray[0],linhaArray[1],linhaArray[2],linhaArray[3]);
				} else {
					deposito.criaCliente(linhaArray[0],linhaArray[1],linhaArray[2],linhaArray[3]);
				}
				
				linhaC++;
			}
	
			/*for (Integer key : deposito.getListaCliente().keySet()) {				
				System.out.print(deposito.getListaCliente().get(key).getIdentificador() + ";" );
			}*/
			deposito.populaMatrizCusto();
			deposito.populaListaDemanda();
			
			ArrayList<Rota> solucao = deposito.buscaTabu();
			
			
			/*System.out.println("---------NOVA SOLUCAO--------");			
			
			for (Rota rota2 : solucao) {
				System.out.print(rota2.getCustoTotal() + " - Rota - ");
				for (Cliente cliente : rota2.getListaCliente()) {
					System.out.print(cliente.getIdentificador() + ";");
				}
				System.out.println("");
			}*/
	}

}
