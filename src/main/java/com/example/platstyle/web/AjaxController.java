package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("api/ajax")
@AllArgsConstructor
public class AjaxController {
    private  ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private StylistRepository stylistRepository;
    private TimeslotRepository timeslotRepository;
    private SupportRepository supportRepository;
    private SupportMessageRepository supportMessageRepository;
    private PortfolioRepository portfolioRepository;

    @RequestMapping(value = "getServiceFee/{serviceID}", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getServiceFee(@PathVariable("serviceID") Long sid) {
        Service service = serviceRepository.findById(sid).orElse(null);
        if(service == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            String portfolioJson = "\"photos\":[";
            List<Portfolio> portfolios = portfolioRepository.findAllByService(service);
            boolean first = true;
            for(Portfolio portfolio: portfolios) {
                if(first)  portfolioJson = portfolioJson + "\"" + portfolio.getPhoto() + "\"";
                else portfolioJson = portfolioJson + ",\"" + portfolio.getPhoto() + "\"";
                first = false;
            }
            portfolioJson += "]";
            try {
                String output="{\"minPrice\":"+ service.getMinPrice()+ ", \"maxPrice\":"+service.getMaxPrice()+", "+portfolioJson+"}";
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(output, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @RequestMapping(value = "getServicesByGender/{gender}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getServicesByGender(@PathVariable("gender") String gender, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Service> services = serviceRepository.findAllByUidANDGender(appUser.getUid(), gender);
        if(services == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {

            String serviceJson ="";
            for (Service service: services) {
                    serviceJson = serviceJson + "<option value='"+service.getSid()+"'>"+service.getServiceName()+"</option>";
            }
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(serviceJson, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }
    @RequestMapping(value = "getServicesBySidAndGender/{sid}/{gender}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getServicesByGender(@PathVariable("sid") Long sid,@PathVariable("gender") String gender, Principal principal) {
        Long uid= stylistRepository.findUidBySid(sid);
        List<Service> services = serviceRepository.findAllByUidANDGender(uid, gender);
        if(services == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            String serviceJson ="";
            for (Service service: services) {
                serviceJson = serviceJson + "<option value='"+service.getSid()+"'>"+service.getServiceName()+"</option>";
            }
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(serviceJson, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "addService", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addService(@RequestParam("gender") String gender,
                                             @RequestParam("serviceName") String serviceName,
                                             @RequestParam("minPrice") double minPrice,
                                             @RequestParam("maxPrice") double maxPrice, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Service service = new Service(null ,gender,serviceName,minPrice,maxPrice, appUser,null);
        serviceRepository.save(service);
        List<Service> services = serviceRepository.findAllByUidANDGender(appUser.getUid(), gender);
        String response ="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "setService", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addService(@RequestParam("serviceID") Long sid,
                                             @RequestParam("minPrice") double minPrice,
                                             @RequestParam("maxPrice") double maxPrice, Principal principal) {
        String response ="";
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Service service = serviceRepository.findById(sid).orElse(null);
        if(service == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        service.setMinPrice(minPrice);
        service.setMaxPrice(maxPrice);
        serviceRepository.save(service);
        response = "success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "getTimeslots", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getTimeslotsByUid(Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        List<Timeslot> timeslots = timeslotRepository.findAllByUid(appUser.getUid());
        if(timeslots == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            StringBuffer timeSlotJson = new StringBuffer();
            boolean first = true;
            timeSlotJson.append("[");
            for (Timeslot timeslot: timeslots) {
                if (!first) {
                    timeSlotJson.append(",");
                }
                timeSlotJson.append("{");
                timeSlotJson.append("\"week\": \""+timeslot.getWeek()+"\", ");
                timeSlotJson.append("\"startTime\": \""+timeslot.getStartTime()+"\", ");
                timeSlotJson.append("\"endTime\": \""+timeslot.getEndTime()+"\" ");
                timeSlotJson.append("}");
                first = false;
            }
            timeSlotJson.append("]");
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(timeSlotJson.toString(), HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "setProfrile", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> setProfrile(@RequestParam("name") String name,
                                              @RequestParam("city") String city,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("email") String email,
                                              @RequestParam("week") String[] weeks,
                                              @RequestParam Map<String,String> searchParams,
                                              Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(appUser == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        Stylist stylist = stylistRepository.findAllByUid(appUser).orElse(null);
        if(!name.equals(""))  stylist.setName(name);
        stylist.setCity(city);
        stylist.setPhone(phone);
        stylist.setEmail(email);
        stylistRepository.save(stylist);
        timeslotRepository.deleteByAppUser(appUser);
        for(String week: weeks) {
            String startTime = week+"Start";
            String endTime = week+"End";
            Timeslot timeslot = new Timeslot(null, week, searchParams.get(startTime), searchParams.get(endTime), appUser);
            timeslotRepository.save(timeslot);
        }
        String response="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "getMessages/{sid}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getMessages(@PathVariable("sid") Long sid, Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Support support = supportRepository.findAllBySid(sid).orElse(null);
        List<Support_message> messages = support.getMessages();
        if(messages == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            String supportJson ="";
            supportJson = "<p>\n" +
                    "                        Order: "+support.getOrderNum()+"<br>\n" +
                    "                        Name: "+support.getUserName()+"<br>\n" +
                    "                        Date: "+support.getFormatCreateDate()+"<br>\n" +
                    "                        Category: "+support.getCategory()+"<br>\n" +
                    "                        Subject: "+support.getSubject()+"<br>\n" +
                    "                    </p>\n" +
                    "                    <div class=\"mt-4 text-start\" style = \"width: 100%; height: 250px; line-height: 3em; overflow:scroll;border: thin #000 solid; padding: 5px;\">\n" +
                    "                        <blockquote id=\"messages\" class=\"clearfix\" style=\"display: block !important\">";
            for (Support_message message: messages) {
                String messageFormatDate = new SimpleDateFormat("MM/dd").format(message.getCreateDate());
                if(message.getSender() != appUser) {
                    supportJson += "<div class=\"stranger text \" mid=\"1\">\n" +
                            "                                <span class=\"hidden_text\">"+message.getSenderName()+":</span>"+message.getMessage()+"\n" +
                            "                                <div class=\"stranger comment\">\n" +
                            "                                    <span class=\"hidden_text\"> (</span>\n" +
                            "                                    <time class=\"timeago\" datetime=\""+message.getCreateDate()+"\">"+messageFormatDate+"</time>\n" +
                            "                                    <span class=\"hidden_text\">)<br></span>\n" +
                            "                                </div>\n" +
                            "                            </div>";
                } else {
                    supportJson += "<div class=\"me text\" id=\"msgcqltdjvyj\" mid=\"0\">\n" +
                            "                                <span class=\"hidden_text\">Me: </span>"+message.getMessage()+"\n" +
                            "                                <div class=\"me comment\">\n" +
                            "                                    <span class=\"hidden_text\">(</span>\n" +
                            "                                    <time class=\"timeago\" datetime=\""+message.getCreateDate()+"\">"+messageFormatDate+"</time>\n" +
                            "                                    <span class=\"hidden_text\">)<br></span>\n" +
                            "                                </div>\n" +
                            "                            </div>";
                }
            }
            supportJson = supportJson + "</blockquote> </div>";
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(supportJson, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "addMessage", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addMessage(@RequestParam("message") String message,
                                              @RequestParam("sid") Long sid,
                                              Principal principal) {
        AppUser appUser = userRepository.findByEmail(principal.getName()).orElse(null);
        Support support = supportRepository.findAllBySid(sid).orElse(null);
        if(appUser == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date date = Date.from(zdt.toInstant());
        Support_message support_message = new Support_message(null, message, date, appUser, support);
        supportMessageRepository.save(support_message);
        String response="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "getStylistListByFilter", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getStylistListByFilter(@RequestParam("city") String[] citys,
                                                         @RequestParam("gender") String[] gender,
                                                         Principal principal){

        //String sql = "SELECT * FROM `stylist` WHERE city LIKE \"%vancouver%\" AND sid IN ( SELECT DISTINCT uid FROM `service` WHERE gender = \"F\");";
        String sql = "SELECT * FROM `stylist`";
        boolean first = true;
        if(citys.length != 0 || gender.length >= 2 ) sql += " WHERE ";
        else {
            sql += " WHERE verify = true";
        }
        if(citys.length != 0) sql += "city LIKE ";
        for(String city: citys) {
            if(!city.isEmpty()) {
                if(first) sql = sql + "\"%" + city +  "%\"";
                else sql = sql + " or city LIKE \"%" + city +  "%\"";
                first = false;
            }
        }
        if(citys.length != 0 && gender.length >= 2 ) sql += " AND";
        if(gender.length >= 2 ) {
            first = true;
            for(String gen: gender) {
                if(first) {
                    if(!gen.isEmpty() ) {
                        sql += " uid IN ( SELECT DISTINCT uid FROM `service` WHERE gender = \"" + gen +"\"";
                        first = false;
                    }
                } else {
                    if(!gen.isEmpty() ) {
                        sql += " OR gender = \"" + gen + "\"";
                        first = false;
                    }
                }
            }
            sql += ");";
        }
        String response = "";
        List<Object[]> stylists = stylistRepository.findByFilter(sql);
        for(Object[] stylist: stylists) {
            response += "<div class=\"col-md-3\">\n" +
                    "        <div class=\"product\">\n" +
                    "              <div class=\"cart-button mt-3 px-2 d-flex justify-content-end align-items-center\">\n" +
                    "                      <span class=\"product_fav \"><i class=\"bi bi-heart\"></i></span>\n" +
                    "               </div>\n" +
                    "               <div class=\"text-center\">\n" +
                    "                    <a href=\"/user/store?stylist="+stylist[0]+"\">\n" +
                    "                    <img src=\""+ stylist[7] +"\" width=\"200\" height=\"200\">\n" +
                    "                    </a>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"d-flex justify-content-between align-items-center\">\n" +
                    "                                <div class=\"about text-start\">\n" +
                    "                                    <h5>"+ stylist[5] +"</h5>\n" +
                    "                                </div>\n" +
                    "                                <span class=\"about-rating bg-primary\">4.5</span>\n" +
                    "                            </div>\n" +
                    "                        </div>\n" +
                    "                    </div>";
        }
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
