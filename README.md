# PlatStyle
This is a platform for hair stylists to showcase their work and for users to book appointments with them.

Ever struggled to find the perfect haircut in Vancouver? Between navigating busy schedules, skyrocketing prices, and limited stylist information, getting a fresh cut can feel like a chore. That's where PlatStyle comes in!

PlatStyle is your one-stop shop for connecting with talented hair stylists across the Vancouver metropolitan area. With our sleek and appUser-friendly app, finding your next haircut is a breeze. It's time to ditch the stress and embrace a world of convenient haircuts with PlatStyle!

### Project Demo
https://platstyle.melodieeee.com/

*Powered by Google Cloud's Managed Platform: PlatStyle is deployed on Google Cloud Platform's (GCP) App Engine.*

### Project Contributors
- [Melody Yu](https://github.com/Melodieeee)
- [Cindy Cheng](https://github.com/chengcindyy)
- [Sam Yang](https://github.com/yangsam810)

## Understanding the Spring PlatStyle application with a few diagrams

[See the presentation here](https://drive.google.com/file/d/1IwFl_pzz91TRSMje6nYe5vPOb6s-oW2g/view?usp=drive_link)

[See the development process here](https://drive.google.com/file/d/1oFFzJpvxwpr-1g9H8S6yAxyiCOR_fKdW/view?usp=drive_link)

## Project Structure

|Spring Boot Configuration | Class or Java property files                                                                                                                                        |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|The Main Class | [PlatStyleApplication](https://github.com/Melodieeee/platstyle/blob/main/src/main/java/com/example/platstyle/PlatStyleApplication.java) |
|Properties Files | [application.properties](https://github.com/Melodieeee/platstyle/blob/main/src/main/resources/application.properties)                                                                      |

## Run the project locally

1. Clone the project
```bash
git clone https://github.com/Melodieeee/platstyle.git
```
1. Config the [applicaton.properties](src/main/resources/application.properties) with your database credentials
1. Once you connect to the database, you can run the project locally at http://localhost:8080/

### Prerequisites

The following items should be installed in your system:

- Java 17 or newer (full JDK, not a JRE)
- [Git command line tool](https://help.github.com/articles/set-up-git)
- Your preferred IDE
    - [Eclipse](https://www.eclipse.org/m2e/)
    - [Spring Tools Suite](https://spring.io/tools) (STS)
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
    - [VS Code](https://code.visualstudio.com)
  
### Test Account
| Role | Email             | Password |
|------|-------------------|----------|
| Admin | admin@gmail.com   | admin |
| Stylist | stylist@gmail.com | stylist |
| Stylist | stylist2@gmail.com | stylist |
| User | appUser@gmail.com   | appUser |


