package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.GameController;
import br.com.fiap.knowball.controller.RefereeController;
import br.com.fiap.knowball.controller.RefereeingController;
import br.com.fiap.knowball.model.Refereeing;


@Component
public class RefereeingModelAssembler implements RepresentationModelAssembler<Refereeing, EntityModel<Refereeing>>{
    
    @Override
    @NonNull
    public EntityModel<Refereeing> toModel(@NonNull Refereeing refereeing) {
        Long gameId = refereeing.getGame().getId();
        Long refereeId = refereeing.getReferee().getId();

        return EntityModel.of(refereeing,
            linkTo(methodOn(RefereeingController.class).getById(gameId, refereeId))
                .withSelfRel()
                .withTitle("Get refereeing details"),

            linkTo(methodOn(RefereeingController.class).getAll())
                .withRel("all-refereeing")
                .withTitle("List all refereeing"),

            linkTo(methodOn(GameController.class).getById(gameId))
                .withRel("game")
                .withTitle("View game details"),

            linkTo(methodOn(RefereeController.class).getById(refereeId))
                .withRel("referee")
                .withTitle("View referee details"),

            linkTo(methodOn(RefereeingController.class).getByGameId(gameId))
                .withRel("game-refereeing")
                .withTitle("List refereeing in this game"),

            linkTo(methodOn(RefereeingController.class).create(null))
                .withRel("create-refereeing")
                .withTitle("Create a new refereeing")
        );
    }
}
