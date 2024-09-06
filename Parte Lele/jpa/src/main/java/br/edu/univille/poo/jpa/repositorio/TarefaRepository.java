package br.edu.univille.poo.jpa.repositorio;

import br.edu.univille.poo.jpa.entidade.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findAllByTituloContaining(String titulo);

    List<Tarefa> findAllByDescricao(String descricao);

    List<Tarefa> findByFinalizado(Boolean finalizado);

    List<Tarefa> findAllByDataPrevisaoFinalAndFinalizadoFalse(LocalDate dataPrevisaoFinal);

    List<Tarefa> findAllByDataFinal(LocalDate dataFinal);

    List<Tarefa> findAllByFinalizadoFalseAndDataPrevistaFinalizacaoBetween(LocalDate inicio, LocalDate fim);
}
