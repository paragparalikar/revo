package com.revo.oms.ui;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;

import com.revo.oms.model.Requisition;
import com.revo.oms.model.RequisitionStatus;
import com.revo.oms.service.RequisitionService;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SpringView(name = "")
@RequiredArgsConstructor
public class RequisitionView extends VerticalLayout implements View {
	private static final long serialVersionUID = -6346081865179988871L;
	
	private static final Set<RequisitionView> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());
	
	public static void refreshAll(@NonNull final Requisition requisition) {
		INSTANCES.forEach(view -> view.grid.getDataProvider().refreshAll());
	}

	private final RequisitionService requisitionService;
	private final Grid<Requisition> grid = new Grid<>();
	
    @PostConstruct
    public void init() {
    	setSizeFull();
    	grid.setSizeFull();
    	grid.addColumn(requisition -> requisition.getKit().getName()).setCaption("Kit");
    	grid.addColumn(requisition -> requisition.getPart().getName()).setCaption("Part");
    	grid.addColumn(requisition -> requisition.getQuantity()).setCaption("Quantity");
        grid.setDataProvider(
        		(order, offset, limit) -> requisitionService.findByStatus(RequisitionStatus.OPEN).stream(), 
        		() -> requisitionService.countByStatus(RequisitionStatus.OPEN));
        addComponent(grid);
        INSTANCES.add(this);
    }
    
    @Override
    public void detach() {
    	INSTANCES.remove(this);
        super.detach();
    }

}