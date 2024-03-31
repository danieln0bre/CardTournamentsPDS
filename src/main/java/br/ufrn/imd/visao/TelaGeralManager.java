package br.ufrn.imd.visao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaGeralManager {

    public static void telaGeralManager(Scanner sc){

        try {
            System.out.println();
            System.out.println("Tela do Gerenciador");
            System.out.println("Selecione uma Opção: ");
            System.out.println("1. Criar evento");
            System.out.println("2. Gerenciar evento");
            System.out.println("3. Retornar para a inicial");
            System.out.print("Digite: ");

            int opcao = sc.nextInt();

            switch (opcao){
                case 1:
                    TelaCriarEvento.criarEvento(sc);
                    break;
                case 2:
                    TelaGerenciarEventos.gerenciarEventos(sc);
                    break;
                case 3:
                    System.out.println("telaInicial()");
                    break;
                default:
                    System.out.println("Opção inválida");
                    telaGeralManager(sc);
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Opção inváldia");;
            sc.nextLine();
            telaGeralManager(sc);
        }

    }
}
