package br.edu.univille.poo.jpa.controller;

import br.edu.univille.poo.jpa.entidade.Tarefa;
import br.edu.univille.poo.jpa.servico.Tarefaservico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;


@RestController()
@RequestMapping("api/tarefa")
public class TarefaRestController {

    @Autowired
    private Tarefaservico tarefaservico;

    @GetMapping
    public List<Tarefa> obterTodos(){
        return tarefaservico.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> obterPeloId(@PathVariable Long id){
        var opt = tarefaservico.obterPeloId(id);
        return opt.map(tarefa -> new ResponseEntity<>(tarefa, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nao-finalizado")
    public List<Tarefa> obterPeloNaoFinalizado(){
        var opt = tarefaservico.obterPeloNaoFinalizado();
        return opt;
    }

    @GetMapping("/{finalizado}")
    public List<Tarefa> obterPeloFinalizado(@PathVariable Boolean finalizado){
        var opt = tarefaservico.obterPeloFinalizado(finalizado);
        return opt;
    }

    @GetMapping("/{tarefaAtrasada}")
    public List<Tarefa> findAllByDataPrevisaoFinalAndFinalizadoFalse(@PathVariable LocalDate dataFinal){
        var opt = tarefaservico.findAllByDataPrevisaoFinalAndFinalizadoFalse(dataFinal);
        return opt;
    }

    @GetMapping("/{finalizartarefa}")
    public ResponseEntity<Tarefa> Finalizartarefa(@PathVariable Long id){
        var opt = tarefaservico.obterPeloId(id);
        return opt.map(tarefa -> new ResponseEntity<>(tarefa, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nao-finalizadas-entre")
    public List<Tarefa> obterNaoFinalizadasEntre(@RequestParam("inicio") LocalDate inicio, @RequestParam("fim") LocalDate fim) {
        return tarefaservico.obterNaoFinalizadasEntre(inicio, fim);
    }

    @PostMapping
    public ResponseEntity<?> incluir(@RequestBody Tarefa tarefa){
        try {
            tarefa = tarefaservico.incluir(tarefa);
            return new ResponseEntity<>(tarefa, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody Tarefa tarefa){
        try {
            tarefa = tarefaservico.atualizar(tarefa);
            return new ResponseEntity<>(tarefa, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluir(@RequestBody Tarefa tarefa){
        try{
            tarefaservico.excluir(tarefa);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // Finalizar uma tarefa
    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarTarefa(@PathVariable Long id) {
        try {
            tarefaservico.finalizarTarefa(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
