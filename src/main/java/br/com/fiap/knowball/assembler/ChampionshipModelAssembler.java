package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.ChampionshipController;
import br.com.fiap.knowball.model.Championship;

@Component
public class ChampionshipModelAssembler implements RepresentationModelAssembler<Championship, EntityModel<Championship>> {

    @Override
    @NonNull
    public EntityModel<Championship> toModel(@NonNull Championship championship) {
        return EntityModel.of(championship,
            linkTo(methodOn(ChampionshipController.class).getById(championship.getId())).withSelfRel().withTitle("Get championship details"),
            linkTo(methodOn(ChampionshipController.class).getAll()).withRel("all-championships").withTitle("List all championships"),
            linkTo(methodOn(ChampionshipController.class).update(championship.getId(), null)).withRel("update-championship").withTitle("Update this championship"),
            linkTo(methodOn(ChampionshipController.class).destroy(championship.getId())).withRel("delete-championship").withTitle("Delete this championship"),
            linkTo(methodOn(ChampionshipController.class).create(null)).withRel("create-championship").withTitle("Create a new championship")
        );
    }
}
