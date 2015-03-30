package com.example.vaadindemo;

import com.example.vaadindemo.model.MyValidator;
import com.example.vaadindemo.model.Bike;
import com.vaadin.annotations.Title;
import com.vaadin.ui.*;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("Aplikacja rowerowa")
public class VaadinApp extends UI {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void init(VaadinRequest request) {
        VerticalLayout mainContainer = new VerticalLayout();
// Table
// ========================================================================
        final BeanItemContainer<Bike> beanContainer = new BeanItemContainer<Bike>(
                Bike.class);
        beanContainer.addBean(new Bike("adam@gmail.com", "Kross", 1950, 17.5, 26));
        beanContainer.addBean(new Bike("bartek@gmail.com", "Cube", 2500, 19, 27.5));
        beanContainer.addBean(new Bike("kacper@gmail.com", "Giant", 2750, 20, 26));
        final Table tabela = new Table();
        tabela.setContainerDataSource(beanContainer);
        tabela.setSelectable(true);
        tabela.setImmediate(true);
// ========================================================================
// form
// ========================================================================
        Bike ktos = new Bike();
        BeanItem<Bike> woot = new BeanItem<Bike>(ktos);
        final FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setBuffered(false);
        fieldGroup.setItemDataSource(woot);

// ========================================================================
// funkcje
// ========================================================================
        HorizontalLayout buttons = new HorizontalLayout();
        final Button btnDodaj = new Button("Dodaj");
        final Button btnUsun = new Button("Usuń");
        final Button btnEdytuj = new Button("Edytuj");
        buttons.addComponent(btnDodaj);
        buttons.addComponent(btnEdytuj);
        buttons.addComponent(btnUsun);
// ========================================================================
// Walidatory =============================================================
        FormLayout formLayout = new FormLayout();
        formLayout.setImmediate(true);
        
        Field<?> markaField = fieldGroup.buildAndBind("Marka", "marka");
        markaField.addValidator(new MyValidator());
        markaField.setRequired(true);
        markaField.addValidator(new StringLengthValidator("Zła długośc", 3, 20, false));

        
        Field<?> emailField = fieldGroup.buildAndBind("E-mail", "email");
        emailField.setRequired(true);
        emailField.addValidator(new EmailValidator("To nie jest E-mail"));

     
        Field<?> cenaField = fieldGroup.buildAndBind("Cena roweru", "cena");
        cenaField.setRequired(true);
        cenaField.addValidator(new DoubleRangeValidator("Nierealna cena roweru", 1.00, 10000.00));
        
        Field<?> rozmiarRamyField = fieldGroup.buildAndBind("Rozmiar ramy", "rozmiarRamy");
        rozmiarRamyField.setRequired(true);
        rozmiarRamyField.addValidator(new DoubleRangeValidator("Błędna wielkość ramy", 
                15.00, 22.00));
        
        Field<?> rozmiarOponField = fieldGroup.buildAndBind("Rozmiar opon", "rozmiarOpon");
        rozmiarOponField.setRequired(true);
        rozmiarOponField.addValidator(new DoubleRangeValidator("Błędny rozmiar opon", 
                26.00, 29.00));

        formLayout.addComponent(markaField);
        formLayout.addComponent(emailField);
        formLayout.addComponent(cenaField);
        formLayout.addComponent(rozmiarRamyField);
        formLayout.addComponent(rozmiarOponField);
// ========================================================================
        mainContainer.addComponent(tabela);
        mainContainer.addComponent(formLayout);
        mainContainer.addComponent(buttons);
        tabela.addValueChangeListener(new ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (tabela.getValue() != null) {
                    Bike temp = (Bike) tabela.getValue();
                    BeanItem<Bike> bike = new BeanItem<Bike>(temp);
                    fieldGroup.setItemDataSource(bike);
                }
            }
        });
        btnDodaj.addClickListener(new ClickListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                Bike toot = ((BeanItem<Bike>) fieldGroup.getItemDataSource()).getBean();
                beanContainer.addBean(new Bike(toot.getEmail(), toot.getMarka(), toot.getCena(),
                        toot.getRozmiarRamy(), toot.getRozmiarOpon()));
            }
        });
        btnUsun.addClickListener(new ClickListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                beanContainer.removeItem(tabela.getValue());
            }
        });
        btnEdytuj.addClickListener(new ClickListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    tabela.refreshRowCache();
                } catch (CommitException e) {
                    e.printStackTrace();
                }
            }
        });
        setContent(mainContainer);
    }
}
