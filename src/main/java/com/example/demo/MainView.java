package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final CustomerRepository repository;
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final Button saveBtn = new Button("Save");
    private final Button deleteBtn = new Button("Delete");

    private Customer selectedCustomer;

    public MainView(CustomerRepository repository) {
        this.repository = repository;

        grid.setColumns("id", "firstName", "lastName");
        refreshGrid();

        grid.asSingleSelect().addValueChangeListener(e -> {
            selectedCustomer = e.getValue();
            if (selectedCustomer != null) {
                firstName.setValue(selectedCustomer.getFirstName());
                lastName.setValue(selectedCustomer.getLastName());
            }
        });

        saveBtn.addClickListener(e -> {
            if (selectedCustomer == null) {
                selectedCustomer = new Customer();
            }
            selectedCustomer.setFirstName(firstName.getValue());
            selectedCustomer.setLastName(lastName.getValue());
            repository.save(selectedCustomer);
            clearForm();
            refreshGrid();
        });

        deleteBtn.addClickListener(e -> {
            if (selectedCustomer != null) {
                repository.delete(selectedCustomer);
                clearForm();
                refreshGrid();
            }
        });

        HorizontalLayout form = new HorizontalLayout(firstName, lastName, saveBtn, deleteBtn);
        add(form, grid);
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        selectedCustomer = null;
    }
}