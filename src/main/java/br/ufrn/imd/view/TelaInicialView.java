package br.ufrn.imd.view;

import java.util.Scanner;

import br.ufrn.imd.controller.TelaInicialController;

public class TelaInicialView {

	public static void start(Scanner sc) {
		System.out.println("Sistema de organizacao de torneios de Card Games");
		
		TelaInicialController.start(sc);
	}
}
