package br.ufrn.imd.visao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaGerenciarEventos {
    public static void gerenciarEventos(Scanner sc){
        System.out.println();

        try {
            for (int i = 0; i < 20; i++){
                System.out.println();
            }
            System.out.println("Selecione a opção desejada para continuar: ");
            System.out.println("1. Tela para editar/deletar evento");
            System.out.println("2. Tela para parear/registrar resultados");
            System.out.println("3. Voltar para a página anterior");
            System.out.print("Digite: ");

            int opcao = sc.nextInt();

            switch (opcao){
                case 1:
                    TelaEditarDeletarEvento.editarDeletarEvento(sc);
                    break;
                case 2:
                    TelaPareamentoRegistrarResultados.parearRegistrarResultados(sc);
                    break;
                case 3:
                    TelaGeralManager.telaGeralManager(sc);
                    break;
                default:
                    System.out.println("Opção inválida");
                    gerenciarEventos(sc);
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida");
            sc.nextLine();
            gerenciarEventos(sc);
        }
    }
}
