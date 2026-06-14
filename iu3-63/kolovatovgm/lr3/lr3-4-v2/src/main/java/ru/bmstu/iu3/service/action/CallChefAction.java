package ru.bmstu.iu3.service.action;

import ru.bmstu.iu3.service.ChefConsultationService;

public class CallChefAction implements Action { // command

    private final ChefConsultationService chefConsultationService;

    public CallChefAction(ChefConsultationService chefConsultationService) {
        this.chefConsultationService = chefConsultationService;
    }

    @Override
    public String description() {
        return "Позвать шеф-повара";
    }

    @Override
    public void execute() {
        chefConsultationService.runConsultationSession();
    }
}
