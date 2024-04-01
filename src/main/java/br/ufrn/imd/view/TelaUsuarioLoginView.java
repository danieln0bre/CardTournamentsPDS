package br.ufrn.imd.view;

import java.util.Scanner;

import br.ufrn.imd.controller.TelaUsuarioLoginController;

public class TelaUsuarioLoginView {

	public static void start(Scanner sc) {
		System.out.println("titulo da janela");
        System.out.println("Selecione uma Opção: ");
        System.out.println("1. Login jogador ");
        System.out.println("2. Login manager");
        System.out.println("3. Cadastrar jogador");
        System.out.println("4. Cadastrar manager");
        
        TelaUsuarioLoginController.start(sc);
	}
}
