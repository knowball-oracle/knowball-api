package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.GameController;
import br.com.fiap.knowball.controller.ParticipationController;
import br.com.fiap.knowball.controller.TeamController;
import br.com.fiap.knowball.model.Participation;

@Component
public class ParticipationModelAssembler implements RepresentationModelAssembler<Participation, EntityModel<Participation>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Participation> toModel(@NonNull Participation participation) {
        Long gameId = participation.getGame().getId();
        Long teamId = participation.getTeam().getId();

        return EntityModel.of(participation,
            linkTo(methodOn(ParticipationController.class).getById(gameId, teamId))
                .withSelfRel()
                .withTitle("Get participation details"),

            linkTo(methodOn(ParticipationController.class).getAll())
                .withRel("all-participations")
                .withTitle("List all participations"),

            linkTo(methodOn(GameController.class).getById(gameId))
                .withRel("game")
                .withTitle("View game details"),

            linkTo(methodOn(TeamController.class).getById(teamId))
                .withRel("team")
                .withTitle("View team details"),

            linkTo(methodOn(ParticipationController.class).getByGame(gameId))
                .withRel("game-participations")
                .withTitle("List participations in this game"),

            linkTo(methodOn(ParticipationController.class).update(null))
                .withRel("update-participation")
                .withTitle("Update this participation"),

            linkTo(methodOn(ParticipationController.class).destroy(gameId, teamId))
                .withRel("delete-participation")
                .withTitle("Delete this participation"),

            linkTo(methodOn(ParticipationController.class).create(null))
                .withRel("create-participation")
                .withTitle("Create a new participation")
        );
    }
}
