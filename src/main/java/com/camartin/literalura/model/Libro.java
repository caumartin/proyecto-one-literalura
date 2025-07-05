package com.camartin.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn (name = "autor_id", nullable = false)
    private Autor autor;
    private String idioma;
    private Double descargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autores().get(0));
        this.idioma = datosLibro.idiomas().get(0).toString();
        this.descargas = datosLibro.descargas();;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getDescargas() {
        return descargas;
    }

    public void setDescargas(Double descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Libro= '" + titulo + '\'' +
                ", autor= " + autor.getNombre() +
                ", idioma= " + idioma +
                ", descargas= " + descargas;
    }
}

