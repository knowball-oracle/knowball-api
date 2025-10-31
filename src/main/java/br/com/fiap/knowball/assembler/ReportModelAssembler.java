package br.com.fiap.knowball.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.fiap.knowball.controller.GameController;
import br.com.fiap.knowball.controller.ReportController;
import br.com.fiap.knowball.model.Report;


@Component
public class ReportModelAssembler implements RepresentationModelAssembler<Report, EntityModel<Report>>{
    
    @Override
    @NonNull
    public EntityModel<Report> toModel(@NonNull Report report) {
        return EntityModel.of(report,
            linkTo(methodOn(ReportController.class).getById(report.getId()))
                .withSelfRel()
                .withTitle("Get report details"),

            linkTo(methodOn(ReportController.class).getAll())
                .withRel("all-reports")
                .withTitle("List all reports"),

            linkTo(methodOn(GameController.class).getById(report.getGame().getId()))
                .withRel("game")
                .withTitle("View game details"),
            
            linkTo(methodOn(ReportController.class).getReportsByStatus(report.getStatus()))
                .withRel("reports-by-status")
                .withTitle("List reports with same status"),
            
            linkTo(methodOn(ReportController.class).create(null))
                .withRel("create-report")
                .withTitle("Create a new report")
        );
    }
}
