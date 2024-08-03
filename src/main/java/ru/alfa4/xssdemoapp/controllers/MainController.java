package ru.alfa4.xssdemoapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfa4.xssdemoapp.data.House;
import ru.alfa4.xssdemoapp.data.User;
import ru.alfa4.xssdemoapp.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final List<House> houses =
      List.of(
          new House(1, 100, "Невский проспект", "Адмиралтейская", 3, 15000),
          new House(2, 85, "Большая Морская улица", "Адмиралтейская", 5, 12000),
          new House(3, 120, "Садовая улица", "Спасская", 2, 18000),
          new House(4, 130, "Кондратьевский проспект", "Лесная", 3, 17000),
          new House(5, 185, "Набережная Обводного канала", "Площадь Александра Невского", 4, 13000),
          new House(6, 140, "Гражданский проспект", "Гражданский проспект", 1, 19000),
          new House(7, 130, "Проспект Непокоренных", "Площадь Мужества", 6, 14000),
          new House(8, 150, "Большая Пушкарская", "Петроградская", 2, 20000),
          new House(9, 120, "Чкаловский проспект", "Чкаловская", 1, 18000),
          new House(10, 100, "Торфяная дорога", "Старая деревня", 2, 17000),
          new House(11, 185, "Савушкина", "Беговая", 1, 12500),
          new House(12, 130, "Новосмоленская набережная", "Приморская", 3, 15000),
          new House(13, 130, "Проспект стачек", "Кировский завод", 2, 18500),
          new House(14, 145, "Московский проспект", "Парк победы", 1, 13500),
          new House(15, 165, "Звёздная", "Звёздная", 3, 16000));

  @GetMapping("/main")
  public String main(
      @RequestParam(name = "search", required = false) String searchInput, Model model) {
    List<House> filteredHouses =
        (searchInput != null && !searchInput.isEmpty())
            ? houses.stream()
                .filter(
                    h ->
                        (h.getMetro().contains(searchInput))
                            || String.valueOf(h.getFloor()).equals(searchInput)
                            || String.valueOf(h.getSquare()).equals(searchInput)
                            || h.getStreet().contains(searchInput)
                            || String.valueOf(h.getPrice()).equals(searchInput))
                .collect(Collectors.toList())
            : houses;
    model.addAttribute("houses", filteredHouses);
    model.addAttribute("searchInput", searchInput);
    return "main";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute User user, Model model) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.createUser(user);
    return "redirect:/login";
  }
}
