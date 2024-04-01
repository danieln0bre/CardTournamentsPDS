package br.ufrn.imd.visao;

import br.ufrn.imd.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaCriarEvento {

    public static void criarEvento(Scanner sc){

        for (int i = 0; i < 20; i++){
            System.out.println();
        }

        try {
            System.out.println("---------------------- CRIAR EVENTO -------------------");
            System.out.println();

            System.out.print("Digite o nome do evento: ");
            sc.nextLine();
            String name = sc.nextLine();

            System.out.print("Digite a data do evento (no formato dd/MM/yyyy): ");
            String dateEvent = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateEvent);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Certifique-se de inserir no formato dd/MM/yyyy.");
                criarEvento(sc);
            }

            System.out.print("Digite a localização: ");
            String location = sc.nextLine();

            System.out.print("Digite o numero de rounds: ");
            int numberOfRounds = sc.nextInt();

            System.out.println();

            Event event = new Event(name, date, location, numberOfRounds);

            System.out.println("Evento criado com sucesso!!!");
            System.out.println(event.toString());

        } catch (InputMismatchException e){
            System.out.println();
            System.out.println("Alguma opção foi digitada incorretamente. Fique atento na hora do preenchimento dos campos.");
            criarEvento(sc);
        }

    }
}
