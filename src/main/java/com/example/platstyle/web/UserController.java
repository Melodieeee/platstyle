package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@AllArgsConstructor
public class UserController {
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private final String UPLOAD_PHOTO_DIR = "src/main/resources/static/img/";
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private StylistRepository stylistRepository;
    private OrderRepository orderRepository;
    private  OrderServiceRepository orderServiceRepository;
    private SupportRepository supportRepository;
    private SupportMessageRepository supportMessageRepository;
    @GetMapping(path = "/")
    public String students() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String login(){ return "login";}

    @GetMapping(path = "/signup")
    public String signup(WebRequest request, Model model){
        model.addAttribute("user", new AppUser());
        return "signup";
    }

    @GetMapping(path = "/forgotpassword")
    public String forgotpassword(){ return "forgotpassword";}

    @PostMapping(path = "/register")
    public String register(AppUser appUser, BindingResult bindingResult,
                           RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "signup";
        } else {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
            appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
            appUser.setRegisterDate(date);
            appUser.setRoles("ROLE_USER");
            userRepository.save(appUser);
            userRepository.flush();
            Customer customer = new Customer();
            customer.setUid(appUser.getUid());
            customerRepository.save(customer);
            attributes.addFlashAttribute("message", "Success register, please login!");
            return "redirect:/login";
        }
    }
    @GetMapping(path = "/user/shop")
    public String home(@RequestParam(name="keyword",defaultValue = "") String
                                   keyword, Model model, Stylist stylists) {
        List<Stylist> stylistList;
        if (keyword.isEmpty()){
            stylistList = stylistRepository.findAllVerifiedStylist();
        }else{
            stylistList = stylistRepository.findAllByName(keyword);
        }
        model.addAttribute("stylistList", stylistList);
        return "user/shop";
    }
    @GetMapping(path = "/user/account")
    public String account(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Customer customer = customerRepository.findById(appUser.getUid()).orElse(null);
        if(appUser ==null) throw new RuntimeException("User does not exist");
        if(customer==null) customer = new Customer();
        model.addAttribute("user", appUser);
        model.addAttribute("customer", customer);
        return "user/account";
    }

    @PostMapping(path="/user/save")
    public String save(Model model, AppUser appUser, Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/account";
        } else {
            Optional<AppUser> opor_user = userRepository.findByEmail(appUser.getEmail());
            AppUser existing = opor_user.get();
            copyNonNullProperties(appUser, existing);
            userRepository.save(existing);
            customer.setUid(existing.getUid());
            customerRepository.save(customer);
            return "redirect:/user/account";
        }
    }

    @GetMapping(path = "/user/support")
    public String support(Model model, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Order> userOrders = orderRepository.findAllByUid(appUser.getUid());
        model.addAttribute("support", new Support());
        model.addAttribute("orders", userOrders);
        return "user/support";
    }


    @GetMapping(path = "/user/supportHistory")
    public String supportManagement(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Support> supportList = supportRepository.findAllByAppUser(appUser);
        model.addAttribute("supportList", supportList);
        return "user/supportHistory";
    }

    @GetMapping(path = "/user/upload")
    public String upload(){
        return "user/upload";
    }

    @PostMapping("/user/account")
    public String uploadPhoto(@RequestParam("photoFile") MultipartFile photoFile, Principal principal, Model model, RedirectAttributes attributes){

        String photoFileName = StringUtils.cleanPath(photoFile.getOriginalFilename());
        try {
            Path path = Paths.get(UPLOAD_PHOTO_DIR + photoFileName);
            Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Customer customer = customerRepository.findById(appUser.getUid()).orElse(null);
        customer.setPhoto("../img/"+photoFileName);
        customerRepository.save(customer);
        attributes.addFlashAttribute("message", "You successfully uploaded " + photoFileName + '!');

        return "redirect:/user/account";
    }

    @PostMapping(path = "/user/upload")
    public String uploadFiles(@RequestParam("idFile") MultipartFile idFile,
                              @RequestParam("workPermitFile") MultipartFile workPermitFile,
                              Principal principal,
                              RedirectAttributes attributes){
        if(idFile.isEmpty() && workPermitFile.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/user/upload";
        }
        String idFileName = StringUtils.cleanPath(idFile.getOriginalFilename());
        String workPermitFileName = StringUtils.cleanPath(workPermitFile.getOriginalFilename());
        try {
            Path path;
            if(!idFile.isEmpty()) {
                path = Paths.get(UPLOAD_DIR + idFileName);
                Files.copy(idFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            if(!workPermitFile.isEmpty()) {
                path = Paths.get(UPLOAD_DIR + workPermitFileName);
                Files.copy(workPermitFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);

        Stylist stylist = appUser.getStylist();
        if(stylist == null) stylist = new Stylist(null, appUser, appUser.getFirstName()+" "+ appUser.getLastName(), appUser.getPhone(), appUser.getEmail(), "", idFileName, workPermitFileName, "", false,0.0,null, null);
        else {
            stylist.setIdentityDocument(idFileName);
            stylist.setWorkPermit(workPermitFileName);
        }
        stylistRepository.save(stylist);
        attributes.addFlashAttribute("message", "You successfully uploaded " + idFileName + ", "+ workPermitFileName +'!');
        return "redirect:/user/upload";
    }

    @GetMapping(path = "/user/cart")
    public String order(Model model, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findAllByAppUserAndStatus(appUser,0).orElse(null);
        Double total = 0.0;
        if(order == null) return "user/cart";
        List<Order_service> services = order.getServices();
        if(services.size() == 0) return "user/cart";
        for (Order_service service : services) {
            total += service.getPrice();
        }
        order.setTotalPrice(total);
        orderRepository.save(order);
        Service service = services.get(0).getService();
        model.addAttribute("stylistName", service.getStylistName());
        model.addAttribute("services", services);
        model.addAttribute("total", "$"+total.toString());
        model.addAttribute("scheduleDate", order.getScheduleDate());
        return "user/cart";
    }

    @GetMapping(path = "/user/checkout")
    public String checkout(Model model, Principal principal){
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findAllByAppUserAndStatus(appUser,0).orElse(null);
        List<Order_service> services = order.getServices();
        Customer customer = customerRepository.findById(appUser.getUid()).orElse(null);
        model.addAttribute("order", order);
        model.addAttribute("services", services);
        model.addAttribute("customer", customer);
        model.addAttribute("payment", new Payment());

        return "user/checkout";
    }

    @PostMapping(path = "/user/checkout")
    public String placeOrder(Payment payment,
                             Order newOrder,
                             Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/checkout";
        } else {
            AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
            Order or_order = orderRepository.findAllByAppUserAndStatus(appUser,0).orElse(null);
            or_order.setAddress(newOrder.getAddress());
            or_order.setNote(newOrder.getNote());
            payment.setOrder(or_order);
            or_order.setPayment(payment);
            Date in = new Date();
            LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            Date date = Date.from(zdt.toInstant());
            payment.setCreateDate(date);
            payment.setAmount((or_order.getTotalPrice() * 0.112 + 3));
            //copyNonNullProperties(or_order,newOrder);
            or_order.setStatus(1);
            orderRepository.save(or_order);
            return "redirect:/user/orderDetail?order="+or_order.getOid();
        }
    }


    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @PostMapping(path="/user/addSupport")
    public String addSupport(Support support,
                             @RequestParam("message") String messageText,
                             @RequestParam("oid") long oid,
                             Principal principal,
                             BindingResult bindingResult,
                             RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return "user/account";
        }

        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findById(oid).orElse(null);

        if (appUser == null || order == null) {
            attributes.addFlashAttribute("error", "Submission failed: Invalid user or order.");
            return "redirect:/user/support";
        }

        support.setAppUser(appUser);
        support.setOrder(order);
        support.setCreateDate(new Date());

        support = supportRepository.save(support);
        supportRepository.flush();

        Support_message message = new Support_message(null, messageText, support.getCreateDate(), appUser, support);
        supportMessageRepository.save(message);

        attributes.addFlashAttribute("message", "Thank you for the submission, we will reply you soon!");
        return "redirect:/user/support";
    }


    @GetMapping("/user/removeOrderService")
    public String removeOrderService(Long id, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findAllByAppUserAndStatus(appUser,0).orElse(null);
        Order_service service = orderServiceRepository.findById(id).orElse(null);
        if(order.getOid() != service.getOrder().getOid()) throw new RuntimeException("Order is not yours");
        System.out.println(service.getPrice());
        orderServiceRepository.delete(service);
        if(order.getServices().size()==0) orderRepository.delete(order);
        return "redirect:/user/cart";
    }

    @PostMapping(path="/user/setScheduleDate")
    public String save(@RequestParam("scheduleDate") String scheduleDate, Principal principal, RedirectAttributes attributes) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findAllByAppUserAndStatus(appUser,0).orElse(null);
        if(scheduleDate.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select the date!");
            return "redirect:/user/cart";
        }
        scheduleDate = scheduleDate.replace('T',' ');
        scheduleDate += ":00";
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(scheduleDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        order.setScheduleDate(date);
        orderRepository.save(order);
        return "redirect:/user/checkout";
    }
}


