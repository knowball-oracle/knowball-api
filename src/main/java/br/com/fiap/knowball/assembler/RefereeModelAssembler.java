package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.RefereeController;
import br.com.fiap.knowball.model.Referee;

@Component
public class RefereeModelAssembler implements RepresentationModelAssembler<Referee, EntityModel<Referee>>{
    
    @Override
    @NonNull
    public EntityModel<Referee> toModel(@NonNull Referee referee) {
        return EntityModel.of(referee,
            linkTo(methodOn(RefereeController.class).getById(referee.getId()))
                .withSelfRel()
                .withTitle("Get referee details"),

            linkTo(methodOn(RefereeController.class).getAll())
                .withRel("all-referees")
                .withTitle("List all referees"),

            linkTo(methodOn(RefereeController.class).update(referee.getId(), null))
                .withRel("update-referee")
                .withTitle("Update this referee"),

            linkTo(methodOn(RefereeController.class).destroy(referee.getId()))
                .withRel("delete-referee")
                .withTitle("Delete this referee"),

            linkTo(methodOn(RefereeController.class).create(null))
                .withRel("create-referee")
                .withTitle("Create a new referee")
        );
    }
}
