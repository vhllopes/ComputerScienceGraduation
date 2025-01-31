import classes.Categoria;
import classes.DisplayLoja;
import classes.Produto;
import classes.ProdutoVirtual;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args){
        String categoria = "C:\\Users\\vitor\\Desktop\\Estudo programação\\Projeto Java\\categorias.txt";           //endereço do arquivo categorias.txt
        String produtoFisico = "C:\\Users\\vitor\\Desktop\\Estudo programação\\Projeto Java\\produtoFisico.txt";    //endereço do arquivo produtoFisico.txt
        String produtoVirtual = "C:\\Users\\vitor\\Desktop\\Estudo programação\\Projeto Java\\produtoVirtual.txt";  //endereço do arquivo produtoVirtual.txt
        DisplayLoja display = new DisplayLoja(categoria, produtoFisico, produtoVirtual);
    }
}
