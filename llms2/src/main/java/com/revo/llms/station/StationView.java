package com.revo.llms.station;

import static com.revo.llms.LlmsConstants.ROUTE_STATIONS;

import javax.annotation.security.PermitAll;

import org.vaadin.klaudeta.PaginatedGrid;

import com.revo.llms.common.MainLayout;
import com.revo.llms.common.TitledView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.security.spring.access.SecuredAccess;

@PermitAll
@PageTitle("Stations")
@SecuredAccess("hasAuthority('page-stations')")
@Route(value = ROUTE_STATIONS, layout = MainLayout.class)
public class StationView extends TitledView {
	private static final long serialVersionUID = 1L;
	
	private final StationEditor stationEditor;
	private final ListDataProvider<Station> stationDataProvider;
	private final PaginatedGrid<Station> grid = new PaginatedGrid<>();

	public StationView(StationService stationService) {
		super(VaadinIcon.TABLE.create(), "Stations");
		stationDataProvider = new ListDataProvider<Station>(stationService.findAll());
		stationEditor = new StationEditor(stationService, stationDataProvider);
 		grid.setDataProvider(stationDataProvider);
		grid.setPageSize(10);
		grid.setPaginatorSize(5);
		createColumns(grid);
		add(grid, stationEditor);
	}

	private void createColumns(PaginatedGrid<Station> grid) {
		grid.addColumn(Station::getId, "id").setHeader("Id");
		grid.addColumn(Station::getName, "name").setHeader("Name");
		grid.addComponentColumn(this::createActionCell)
			.setSortable(false)
			.setTextAlign(ColumnTextAlign.END)
			.setAutoWidth(true);
	}
	
	private Component createActionCell(Station station) {
		final Button button = new Button("Rename", VaadinIcon.EDIT.create());
		button.addClickListener(event -> stationEditor.open(station));
		final HorizontalLayout container = new HorizontalLayout(button);
		container.setWidthFull();
		container.setJustifyContentMode(JustifyContentMode.END);
		return container;
	}
}
