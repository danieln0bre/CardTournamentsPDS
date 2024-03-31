package br.ufrn.imd.visao;

import java.util.Scanner;

public class TelaCriarEvento {
    public static void criarEvento(Scanner sc){
       for (int i = 0; i < 20; i++){
           System.out.println();
       }
        System.out.println("----------------------------------------");
        System.out.println("Criar evento");
        System.out.println("----------------------------------------");
        TelaGeralManager.telaGeralManager(sc);
    }
}
