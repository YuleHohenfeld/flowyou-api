package com.rotina.rotina_api.treino.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exercicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "treino_id", nullable = false)
    private Long treinoId;

    @Column(nullable = false)
    private String nome;

    private Integer series;

    private String repeticoes;

    @Column(name = "tempo_segundos")
    private Integer tempoSegundos;

    @Column(name = "midia_path")
    private String midiaPath;

    @Column(nullable = false)
    private Integer ordem;
}
