package br.edu.univille.poo.jpa.servico;

import br.edu.univille.poo.jpa.entidade.Tarefa;
import br.edu.univille.poo.jpa.repositorio.TarefaRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class Tarefaservico {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> obterTodos() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> obterPeloId(Long id) {
        return tarefaRepository.findById(id);
    }

    public List<Tarefa> obterPeloNaoFinalizado() {
            return tarefaRepository.findByFinalizado(false);
    }//Dúvida

    public List<Tarefa> obterPeloFinalizado(Boolean finalizado) {
            return tarefaRepository.findByFinalizado(true);
    }//Dúvida

    public List<Tarefa> findAllByDataPrevisaoFinalAndFinalizadoFalse(LocalDate dataFinal) {
        LocalDate dataAtual = LocalDate.now();
        return tarefaRepository.findAllByDataFinal(dataFinal);
    }//Dúvida

    public List<Tarefa> obterNaoFinalizadasEntre(LocalDate inicio, LocalDate fim) {
        return tarefaRepository.findAllByFinalizadoFalseAndDataPrevistaFinalizacaoBetween(inicio, fim);
    }



    public Tarefa incluir(Tarefa tarefa) {
        tarefa.setId(0);
        if (tarefa.getTitulo() == null || tarefa.getTitulo().length() < 5) {
            throw new RuntimeException("O título deve conter pelo menos 5 caracteres.");
        }
        if(tarefa.getDataPrevisaoFinal() == null){
            throw new RuntimeException("Data de Previsão de Finalização é obrigatória.");
        }
        if(!tarefaRepository.findAllByTituloContaining(tarefa.getTitulo()).isEmpty()){
        throw new RuntimeException("Está sem titulo definido.");
        }
        if(!tarefaRepository.findAllByDataPrevisaoFinalAndFinalizadoFalse(tarefa.getDataPrevisaoFinal()).isEmpty()){
        throw new RuntimeException("Data de Previsão está Vazia.");
        }
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Tarefa tarefa) {
    Tarefa antigo = tarefaRepository.findById(tarefa.getId()).orElse(null);
    if(antigo == null){
        throw new RuntimeException("Pessoa não foi encontrada.");
    }
    if (antigo.isFinalizado()) {
            throw new RuntimeException("Tarefas finalizadas não podem ser modificadas.");
    }
    antigo.setTitulo(tarefa.getTitulo());
    antigo.setDescricao(tarefa.getDescricao());
    antigo.setDataPrevisaoFinal(tarefa.getDataPrevisaoFinal());
    antigo.setDataFinal(tarefa.getDataFinal());
    if(Strings.isBlank(tarefa.getTitulo())){
        throw new RuntimeException("Titulo não informado.");
    }
    if(Strings.isBlank(tarefa.getDescricao())){
        throw new RuntimeException("Descrição não informado.");
    }
    if(tarefa.getDataPrevisaoFinal() == null){
        throw new RuntimeException("Previsao Final não informado.");
    }
    if(tarefa.getDataFinal() == null){
        throw new RuntimeException("Previsao Final não informado.");
    }
    for(var p : tarefaRepository.findAllByTituloContaining(tarefa.getTitulo())){
        if(antigo.getId() != p.getId()){
            throw new RuntimeException("Nome já está cadastrado.");
        }
    }
    return tarefaRepository.save(antigo);
    }

    public void excluir(Tarefa tarefa) {
    var antigo = tarefaRepository.findById(tarefa.getId()).orElse(null);
    if(antigo == null){
        throw new RuntimeException("Pessoa não encontrada.");
    }
    tarefaRepository.delete(antigo);
    }

    public void finalizarTarefa(Long id) {
        var tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa == null) {
            throw new RuntimeException("Tarefa não encontrada.");
        }
        if (tarefa.isFinalizado()) {
            throw new RuntimeException("Tarefa já está finalizada.");
        }
        LocalDate dataAtual = LocalDate.now();
        tarefa.setFinalizado(true);
        tarefa.setDataFinal(dataAtual);
        tarefaRepository.save(tarefa);
    }
}
