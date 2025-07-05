package com.camartin.literalura.repository;

import com.camartin.literalura.model.Autor;
import com.camartin.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query(value = "SELECT * FROM libros", nativeQuery = true)
    //@Query("SELECT l FROM Libro l")
    List<Libro> listarLibros();

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento<:ano AND a.fechaMuerte>:ano")
    List<Autor> buscarAutoresVivos(int ano);

    @Query("SELECT l FROM Libro l WHERE l.idioma=:idioma")
    List<Libro> buscarLibrosPorIdioma(String idioma);

    @Query("SELECT a FROM Autor a WHERE a.nombre=:nombre")
    Autor autorExistente(String nombre);

    @Query("SELECT l FROM Libro l WHERE l.titulo=:titulo")
    Libro libroExistente(String titulo);

    @Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> top10Descargas();

    @Query("SELECT a FROM Autor a WHERE a.nombre ILIKE %:clave%")
    List<Autor> buscarAutorPorNombre(String clave);

}
