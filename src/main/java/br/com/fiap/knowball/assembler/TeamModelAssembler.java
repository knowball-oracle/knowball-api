package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.TeamController;
import br.com.fiap.knowball.model.Team;

@Component
public class TeamModelAssembler implements RepresentationModelAssembler<Team, EntityModel<Team>>{
    
    @Override
    @NonNull
    public EntityModel<Team> toModel(@NonNull Team team) {
        return EntityModel.of(team,
            linkTo(methodOn(TeamController.class).getById(team.getId()))
                .withSelfRel()
                .withTitle("Get team details"),

            linkTo(methodOn(TeamController.class).getAll())
                .withRel("all-teams")
                .withTitle("List all teams"),

            linkTo(methodOn(TeamController.class).update(team.getId(), null))
                .withRel("update-team")
                .withTitle("Update this team"),

            linkTo(methodOn(TeamController.class).destroy(team.getId()))
                .withRel("delete-team")
                .withTitle("Delete this team"),

            linkTo(methodOn(TeamController.class).create(null))
                .withRel("create-team")
                .withTitle("Create a new team")
        );
    }
}
