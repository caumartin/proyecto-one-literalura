package com.camartin.literalura.principal;

import com.camartin.literalura.model.Autor;
import com.camartin.literalura.model.DatosConsulta;
import com.camartin.literalura.model.DatosLibro;
import com.camartin.literalura.model.Libro;
import com.camartin.literalura.repository.AutorRepository;
import com.camartin.literalura.service.ConsumoAPI;
import com.camartin.literalura.service.ConvierteDatos;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();

    private AutorRepository repositorio;

    private Libro libro;
    private Autor autor;

    public Principal(AutorRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                
                APLICACION LITERALURA
                
                1 - Buscar libros por palabra clave
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                              
                0 - Salir
                """;
            System.out.println(menu);

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarLibro() {

        String palabraClave;
        String json;
        final String URL_BASE = "https://gutendex.com/books/";

        List<DatosLibro> resultado = new ArrayList();

        Autor autorExiste = null;
        Libro libroExiste = null;

        System.out.println("Ingrese palabra clave de búsqueda:");
        palabraClave = teclado.nextLine();

        json = consumoApi.obtenerDatos(URL_BASE+"?search="+palabraClave);
        ConvierteDatos conversor = new ConvierteDatos();
        DatosConsulta datos = conversor.obtenerDatos(json, DatosConsulta.class);

        int resultados = datos.cantidadResultados();

        if (resultados>0) {
            System.out.println("Resultados con esa clave: " + datos.cantidadResultados() + ". Solo se guardará el primero de la lista.");

            resultado.add(datos.resultados().get(0));

            String libroEncontrado = resultado.get(0).titulo();
            String autorEncontrado = resultado.get(0).autores().get(0).nombre();

            System.out.println("Libro encontrado: " + libroEncontrado);
            System.out.println("Autor: " + autorEncontrado);

            try {
                autorExiste = repositorio.autorExistente(autorEncontrado);
            } catch (Exception e) {
                System.out.println("Error leyendo Postgres");
            }

            if (autorExiste==null) {
                //System.out.println("Autor no existe en DB, agregar libro y autor");

                libro = new Libro(resultado.get(0));
                autor = new Autor(resultado.get(0).autores().get(0));
                autor.setLibros(libro);
                repositorio.save(autor);

            } else {
                System.out.println("Autor ya existe en DB");
                try {
                    libroExiste = repositorio.libroExistente(libroEncontrado);
                } catch (Exception e) {
                    System.out.println("Error leyendo Postgres");
                }

                if (libroExiste==null) {
                    System.out.println("Autor ya existe en DB, pero ese libro no, agregar");
                    libro = new Libro(resultado.get(0));
                    autorExiste.setLibros(libro);
                    repositorio.save(autorExiste);

                } else {
                    System.out.println("Ese libro ya existe en DataBase");
                }
            }

        } else {
            System.out.println("No se encontraron libros con esa clave de búsqueda");
        }
    }

    private void listarLibros() {

        System.out.println("\nLIBROS GUARDADOS EN DATABASE\n");
        List<Libro> listadoLibros = repositorio.listarLibros();
        listadoLibros.stream()
                        .forEach(System.out::println);
    }

    private void listarAutores() {

        System.out.println("\nAUTORES GUARDADOS EN DATABASE\n");

        List<Autor> listadoAutores = repositorio.findAll();
        listadoAutores.stream()
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {

        int ano;

        System.out.println("Ingrese año de búsqueda:");
        ano = teclado.nextInt();

        List<Autor> listadoAutores = repositorio.buscarAutoresVivos(ano);
        listadoAutores.stream()
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {

        String idioma;

        System.out.println("Ingrese idioma de búsqueda:");
        idioma = teclado.nextLine();

        List<Libro> listadoLibros = repositorio.buscarLibrosPorIdioma(idioma);
        listadoLibros.stream()
                .forEach(System.out::println);
    }

}