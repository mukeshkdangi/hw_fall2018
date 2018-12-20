import { style, trigger, transition, animate, state} from "@angular/animations";

export const CustomeFadeInAnimation = trigger("CustomeFadeInAnimation", [
  transition("* => *", [style({ opacity: 0 }), 
    animate(".6s ease-in", style({ opacity: 1 }))])
]);