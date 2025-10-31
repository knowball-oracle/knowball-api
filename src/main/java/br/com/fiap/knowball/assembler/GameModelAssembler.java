package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.ChampionshipController;
import br.com.fiap.knowball.controller.GameController;
import br.com.fiap.knowball.model.Game;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>>{
    
    @Override
    @NonNull
    public EntityModel<Game> toModel(@NonNull Game game) {
        return EntityModel.of(game,
            linkTo(methodOn(GameController.class).getById(game.getId()))
                .withSelfRel()
                .withTitle("Get game details"),

            linkTo(methodOn(GameController.class).getAll())
                .withRel("all-games")
                .withTitle("List all games"),

            linkTo(methodOn(GameController.class).getByChampionship(game.getChampionship().getId()))
                .withRel("championship-games")
                .withTitle("List games from this championship"),

            linkTo(methodOn(ChampionshipController.class).getById(game.getChampionship().getId()))
                .withRel("championship")
                .withTitle("View championship details"),

            linkTo(methodOn(GameController.class).update(game.getId(), null))
                .withRel("update-game")
                .withTitle("Update this game"),

            linkTo(methodOn(GameController.class).destroy(game.getId()))
                .withRel("delete-game")
                .withTitle("Delete this game"),
                
            linkTo(methodOn(GameController.class).create(null))
                .withRel("create-game")
                .withTitle("Create a new game")
        );
    }
}
