import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Veiculo {
    private String marca;
    private String modelo;
    private int ano;
    private double preco;

    public Veiculo(String marca, String modelo, int ano, double preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
    }
}

class Carro extends Veiculo {
    private int numPortas;

    public Carro(String marca, String modelo, int ano, double preco, int numPortas) {
        super(marca, modelo, ano, preco);
        this.numPortas = numPortas;
    }
}

class Motocicleta extends Veiculo {
    private int cilindradas;

    public Motocicleta(String marca, String modelo, int ano, double preco, int cilindradas) {
        super(marca, modelo, ano, preco);
        this.cilindradas = cilindradas;
    }
}

interface Armazenamento {
    void adicionarVeiculo(Veiculo veiculo);
    List<Veiculo> recuperarVeiculos();
}

class BancoDeDadosArmazenamento implements Armazenamento {
    private List<Veiculo> estoque = new ArrayList<>();

    @Override
    public void adicionarVeiculo(Veiculo veiculo) {
        estoque.add(veiculo);
    }

    @Override
    public List<Veiculo> recuperarVeiculos() {
        return estoque;
    }
}

class ArquivoArmazenamento implements Armazenamento {
    private String arquivo;

    public ArquivoArmazenamento(String arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public void adicionarVeiculo(Veiculo veiculo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write(veiculo.getMarca() + ", " + veiculo.getModelo() + ", " + veiculo.getAno() + ", " + veiculo.getPreco() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Veiculo> recuperarVeiculos() {
        List<Veiculo> estoque = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dados = line.split(", ");
                String marca = dados[0];
                String modelo = dados[1];
                int ano = Integer.parseInt(dados[2]);
                double preco = Double.parseDouble(dados[3]);
                Veiculo veiculo = new Veiculo(marca, modelo, ano, preco);
                estoque.add(veiculo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return estoque;
    }
}

class Concessionaria {
    private Armazenamento armazenamento;

    public Concessionaria(Armazenamento armazenamento) {
        this.armazenamento = armazenamento;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        List<Veiculo> estoque = recuperarVeiculos();
        if (!estoque.contains(veiculo)) {
            armazenamento.adicionarVeiculo(veiculo);
        } else {
            System.out.println("Este veículo já está no estoque.");
        }
    }

    public List<Veiculo> recuperarVeiculos() {
        return armazenamento.recuperarVeiculos();
    }
}

public class Main {
    public static void main(String[] args) {
        Armazenamento armazenamento = new ArquivoArmazenamento("estoque.txt");
        Concessionaria concessionaria = new Concessionaria(armazenamento);
        Veiculo carro1 = new Carro("Toyota", "Corolla", 2022, 25000.0, 4);
        Veiculo moto1 = new Motocicleta("Honda", "CBR 600", 2021, 12000.0, 600);
        concessionaria.adicionarVeiculo(carro1);
        concessionaria.adicionarVeiculo(moto1);
        List<Veiculo> estoque = concessionaria.recuperarVeiculos();
        for (Veiculo veiculo : estoque) {
            System.out.println("Marca: " + veiculo.getMarca() + ", Modelo: " + veiculo.getModelo() + ", Ano: " + veiculo.getAno() + ", Preço: R$" + veiculo.getPreco());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a marca do veículo: ");
        String novaMarca = scanner.nextLine();
        System.out.print("Digite o modelo do veículo: ");
        String novoModelo = scanner.nextLine();
        System.out.print("Digite o ano do veículo: ");
        int novoAno = Integer.parseInt(scanner.nextLine());
        System.out.print("Digite o preço do veículo: ");
        double novoPreco = Double.parseDouble(scanner.nextLine());
        Veiculo novoVeiculo = new Veiculo(novaMarca, novoModelo, novoAno, novoPreco);
        concessionaria.adicionarVeiculo(novoVeiculo);
        List<Veiculo> estoqueAtualizado = concessionaria.recuperarVeiculos();
        System.out.println("\nEstoque atualizado:");
        for (Veiculo veiculo : estoqueAtualizado) {
            System.out.println("Marca: " + veiculo.getMarca() + ", Modelo: " + veiculo.getModelo() + ", Ano: " + veiculo.getAno() + ", Preço: R$" + veiculo.getPreco());
        }
    }
}