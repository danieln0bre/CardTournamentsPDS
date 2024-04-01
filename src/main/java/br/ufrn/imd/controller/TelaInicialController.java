package br.ufrn.imd.controller;

import java.util.Scanner;

import br.ufrn.imd.view.TelaUsuarioLoginView;

public class TelaInicialController {
	
	public static void start(Scanner sc) {
		
		int opcao = sc.nextInt();
		
		switch (opcao){
        case 1:
            TelaUsuarioLoginView.start(sc);
            break;
        default:
            System.out.println("Opção inválida");
            start(sc);
            break;
    }
	}
	
	// usuario manda input
	// controler chama TelaUsuarioLoginView

}
