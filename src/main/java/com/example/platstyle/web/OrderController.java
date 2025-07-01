package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private UserRepository userRepository;
    private StylistRepository stylistRepository;
    private OrderRepository orderRepository;
    private FeedbackRepository feedbackRepository;
    private  CustomerRepository customerRepository;

    @GetMapping(path = "/user/orders")
    public String orders(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Order> orderList = orderRepository.findAllByUid(appUser.getUid());
        model.addAttribute("orderList", orderList);
        return "user/orders";
    }

    @GetMapping(path = "/user/orderDetail")
    public String orderDetail(Model model, @RequestParam(name="order",defaultValue = "0") long oid, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(oid == 0) return "redirect:/user/shop";
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        if(order.getAppUser().getUid() != appUser.getUid()) return "redirect:/user/shop";
        List<Order_service> services = order.getServices();
        model.addAttribute("order", order);
        model.addAttribute("services", services);
        model.addAttribute("feedback", new Feedback());
        return "user/orderDetail";
    }

    @GetMapping(path = "/stylist/appointments")
    public String appointments(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = appUser.getStylist();
        List<Order> appointmentList = orderRepository.findAllByStylistAndStatusBetween(stylist, 7,100);
        model.addAttribute("appointmentList", appointmentList);
        return "stylist/appointments";
    }

    @GetMapping(path = "/stylist/ongoingAppointments")
    public String ongoingAppointments(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = appUser.getStylist();
        List<Order> appointmentList = orderRepository.findAllByStylistAndStatusBetween(stylist, 1,6);
        model.addAttribute("appointmentList", appointmentList);
        return "stylist/ongoingAppointments";
    }

    @GetMapping(path = "/stylist/manageAppointment")
    public String manageAppointment(Model model, @RequestParam(name="order",defaultValue = "0") long oid, Principal principal){
        if(oid == 0) return "redirect:/user/shop";
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        if(order.getStylist().getSid() != appUser.getStylistId()) return "redirect:/user/shop";
        List<Order_service> services = order.getServices();
        Feedback feedback = order.getFeedback();
        Customer customer = customerRepository.findById(order.getAppUser().getUid()).orElse(null);
        if(feedback == null) feedback = new Feedback(null,"",new Date(),0,null,null);
        model.addAttribute("order", order);
        model.addAttribute("services", services);
        model.addAttribute("feedback", feedback);
        model.addAttribute("customer", customer);
        return "stylist/manageAppointment";
    }
    @GetMapping("/user/cancelOrder")
    public String cancelOrder(Long id, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(id == 0) return "redirect:/user/shop";
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        if(order.getAppUser().getUid() != appUser.getUid()) {
            return "redirect:/user/shop";
        }
        order.setStatus(99);
        orderRepository.save(order);
        return "redirect:/user/orders";
    }

    @GetMapping("/stylist/cancelAppointment")
    public String cancelAppointment(Long id, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(id == 0) return "redirect:/user/shop";
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        if(order.getStylist().getSid() != appUser.getStylistId()) {
            return "redirect:/user/shop";
        }
        order.setStatus(99);
        orderRepository.save(order);
        return "redirect:/stylist/ongoingAppointments";
    }

    @RequestMapping(value = "/stylist/acceptAppointment/{oid}", method = RequestMethod.POST)
    public String acceptAppointment(@PathVariable("oid") long oid,
                                    @RequestParam("acceptSelect") int status, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getStylistId()!=order.getStylist().getSid()) return "redirect:/user/home";
        if(status == 1) {
            order.setStatus(2);
        } else if(status == 2) {
            order.setStatus(99);
        }
        orderRepository.save(order);
        return "redirect:/stylist/manageAppointment?order="+oid;
    }

    @RequestMapping(value = "/stylist/confirmPrice/{oid}", method = RequestMethod.POST)
    public String stylistConfirmPrice(@PathVariable("oid") long oid, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getStylistId()!=order.getStylist().getSid()) return "redirect:/user/home";
        order.setStatus(4);
        orderRepository.save(order);
        return "redirect:/stylist/manageAppointment?order="+oid;
    }

    @RequestMapping(value = "/stylist/completeService/{oid}", method = RequestMethod.POST)
    public String completeService(@PathVariable("oid") long oid,
                                    @RequestParam("completeSelect") int status, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getStylistId()!=order.getStylist().getSid()) {
            return "redirect:/user/home";
        }
        if(status == 1) {
            order.setStatus(6);
        } else if(status == 2) {
            order.setStatus(97);
        }
        orderRepository.save(order);
        return "redirect:/stylist/manageAppointment?order="+oid;
    }

    @RequestMapping(value = "/user/confirmArrive/{oid}", method = RequestMethod.POST)
    public String userconfirmArrive(@PathVariable("oid") long oid,
                                @RequestParam("arriveSelect") int status, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getUid() != order.getAppUser().getUid()) return "redirect:/user/home";
        if(status == 1) {
            order.setArriveTime(new Date());
            order.setStatus(3);
        } else if(status == 2) {
            order.setStatus(98);
        }
        orderRepository.save(order);
        return "redirect:/user/orderDetail?order="+oid;
    }

    @RequestMapping(value = "/user/confirmPrice/{oid}", method = RequestMethod.POST)
    public String userConfirmPrice(@PathVariable("oid") long oid, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getUid() != order.getAppUser().getUid()) return "redirect:/user/home";
        order.setStatus(5);
        orderRepository.save(order);
        return "redirect:/user/orderDetail?order="+oid;
    }

    @RequestMapping(value = "/user/confirmService/{oid}", method = RequestMethod.POST)
    public String confirmService(@PathVariable("oid") long oid,
                                 @RequestParam("completeSelect") int status,
                                 @RequestParam("tip") double tip, Principal principal) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getUid() != order.getAppUser().getUid()) return "redirect:/user/home";
        Stylist stylist= order.getStylist();
        if(status == 1) {
            order.setFinishTime(new Date());
            if(tip < 0) tip = 0;
            order.setTip(tip);
            order.setStatus(7);
            double balance = stylist.getBalance();
            stylist.setBalance(balance + tip + (order.getTotalPrice()*0.9));
            stylistRepository.save(stylist);
        } else if(status == 2) {
            order.setStatus(96);
        }
        orderRepository.save(order);
        return "redirect:/user/orderDetail?order="+oid;
    }

    @RequestMapping(value = "/user/feedback/{oid}", method = RequestMethod.POST)
    public String submitFeedback(@PathVariable("oid") long oid,
                                 @RequestParam("rate") double rateValue,
                                 @RequestParam("comment") String comment,
                                 Principal principal,
                                 Feedback feedback) {
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser.getUid() != order.getAppUser().getUid()) return "redirect:/user/home";

        feedback.setOrder(order);
        feedback.setStylist(order.getStylist());
        feedback.setRate(rateValue);
        feedback.setComment(comment);
        feedback.setCreateDate(new Date());

        feedbackRepository.save(feedback);

        order.setStatus(8);
        orderRepository.save(order);
        return "redirect:/user/orderDetail?order="+oid;
    }



}
