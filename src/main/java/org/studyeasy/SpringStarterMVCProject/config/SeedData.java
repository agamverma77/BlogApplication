package org.studyeasy.SpringStarterMVCProject.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Authority;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.AuthorityService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;
import org.studyeasy.SpringStarterMVCProject.util.constants.Privilages;
import org.studyeasy.SpringStarterMVCProject.util.constants.Roles;
@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;
    @Override
    public void run(String... args) throws Exception {// String ... means this method can reveive any number of arguments including 0
        for(Privilages auth:Privilages.values())//this will iterate on enum & keep on giving values of enum
        {
            //we'll add the objects
            Authority authority=new Authority();
            authority.setId(auth.getAuthorityId());//we'll set the values in this Authority
            authority.setName(auth.getAuthorityString());
            //now we'll save
            //to save we'll autowire the authorityservice
            authorityService.save(authority);//we'll save the authority
        }


       Account account01=new Account();
       Account account02=new Account();
       Account account03=new Account();
       Account account04=new Account();

       account01.setEmail("user@user.com");
       account01.setPassword("pass987");
       account01.setFirstname("user");
       account01.setLastname("lastname");
       account01.setAge(25);
       account01.setDate_of_birth(LocalDate.parse("1990-01-01"));
       account01.setGender("Male");

       account02.setEmail("admin@admin.com");
       account02.setPassword("pass987");
       account02.setFirstname("admin");
       account02.setLastname("lastname");
       account02.setRole(Roles.ADMIN.getRole());
       account02.setAge(30);
       account02.setDate_of_birth(LocalDate.parse("1985-01-01"));
       account02.setGender("Male");

       account03.setEmail("editor@editor.com");
       account03.setPassword("pass987");
       account03.setFirstname("editor");
       account03.setLastname("lastname");
       account03.setRole(Roles.EDITOR.getRole());
       account03.setAge(25);
       account03.setDate_of_birth(LocalDate.parse("1985-01-01"));
       account03.setGender("Female");

       account04.setEmail("super_editor@editor.com");
       account04.setPassword("pass987");
       account04.setFirstname("super_editor");
       account04.setLastname("lastname");
       account04.setRole(Roles.EDITOR.getRole());
       account04.setAge(35);
       account04.setDate_of_birth(LocalDate.parse("1980-01-01"));
       account04.setGender("Male");


       Set<Authority> authorities=new HashSet<>();
       authorityService.findbyId(Privilages.RESET_ANY_USER_PASSWORD.getAuthorityId()).ifPresent(authorities::add);//if we get the value we add it on set
       authorityService.findbyId(Privilages.ACCESS_ADMIN_PANEL.getAuthorityId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);





       accountService.save(account01);
       accountService.save(account02);
       accountService.save(account03);
       accountService.save(account04);
       




       List<Post> posts=postService.getAll();//we'll check whether there are posts in db, if no posts then we'll add, if posts there then we won't do anything, i.e. if posts is empty we'll add else don't do anything
       if(posts.size()==0)
       {
        //add seed data
        //add 1 post to start with
        Post post01=new Post();
        post01.setTitle("About Spring Boot");
        post01.setBody("Spring Boot is an open-source Java framework used for programming standalone, production-grade Spring-based applications with a bundle of libraries that make project startup and management easier.[3] Spring Boot is a convention-over-configuration extension for the Spring Java platform intended to help minimize configuration concerns while creating Spring-based applications.[4][5] The application can still be adjusted for specific needs, but the initial Spring Boot project provides a preconfigured \"opinionated view\" of the best configuration to use with the Spring platform and selected third-party libraries.[6][7]\r\n" + //
                        "\r\n" + //
                        "Spring Boot can be used to build microservices, web applications, and console applications.[3][8]\r\n" + //
                        "\r\n" + //
                        "Features\r\n" + //
                        "Embedded Tomcat, Jetty or Undertow web application server.[9]\r\n" + //
                        "Provides opinionated 'starter' Project Object Models (POMs) for the build tool. The only build tools supported are Maven and Gradle.[10][11]\r\n" + //
                        "Automatic configuration of the Spring Application.[12]\r\n" + //
                        "Provides production-ready[4] functionality such as metrics,[13] health checks,[13] and externalized configuration.[14]\r\n" + //
                        "No code generation is required.[9]\r\n" + //
                        "No XML configuration is required.[10]\r\n" + //
                        "Optional support for Kotlin and Apache Groovy in addition to Java.[3][15]\r\n" + //
                        "Bootstrapping DispatcherServlet\r\n" + //
                        "See also: Spring Framework § Configuration of DispatcherServlet\r\n" + //
                        "Spring Boot does not require manual configuration of the DispatcherServlet, since it automatically configures the application based on the configuration it detects. [16]\r\n" + //
                        "\r\n" + //
                        "SpringBootServletInitializer\r\n" + //
                        "Spring Boot has a class SpringBootServletInitializer, which is a specialization of the WebApplicationInitializer.[16] This SpringBootServletInitializer is an out-of-the-box implementation of WebApplicationInitializer, which eliminates the need for the developer to construct their own implementation of the WebApplicationInitializer class.[16]\r\n" + //
                        "\r\n" + //
                        "Configuration properties\r\n" + //
                        "The configuration properties for the Spring Boot application can be specified in the application.properties or application.yml file.[16] Examples of properties that can be included in this file include the server.port and spring.application.name properties.[16]\r\n" + //
                        "\r\n" + //
                        "Autoconfiguration\r\n" + //
                        "@SpringBootApplication\r\n" + //
                        "Spring boot has an annotation, @SpringBootApplication, which allows the Spring Boot application to autoconfigure third-party libraries and detected features found on the classpath.[16] As an example, the class that has the @SpringBootApplication annotation can extend the SpringBootServerInitializer class if the application is packaged and deployed as a WAR file.[16]\r\n" + //
                        "\r\n" + //
                        "The @SpringBootApplication annotation combines three Spring-specific annotations: @SpringBootConfiguration, @EnableAutoConfiguration and @ComponentScan.[17]\r\n" + //
                        "\r\n" + //
                        "@SpringBootConfiguration\r\n" + //
                        "The @SpringBootConfiguration annotation is a specialization of the Spring-specific @Configuration annotation.[17] The class with the @SpringBootConfiguration is marked as the configuration class for the Spring Boot application.[17]\r\n" + //
                        "\r\n" + //
                        "@EnableAutoConfiguration\r\n" + //
                        "The @EnableAutoConfiguration annotation is Spring-specific annotation that enables the Spring Boot automatic configuration. [17]\r\n" + //
                        "\r\n" + //
                        "Actuator\r\n" + //
                        "The Spring Boot Actuator allows for monitoring and management capabilities for the Spring Boot Application.[18] A major advantage of using the Spring Boot Actuator is that it implements a number of production-ready features without requiring the developer to construct their own implementations.[18]\r\n" + //
                        "\r\n" + //
                        "If Maven is used as the build tool, then the spring-boot-starter-actuator dependency can be specified in the pom.xml configuration file.[19]\r\n" + //
                        "\r\n" + //
                        "Integration with Spring Framework Modules\r\n" + //
                        "Spring Boot has a number of existing Spring Framework Modules.\r\n" + //
                        "\r\n" + //
                        "Spring Security\r\n" + //
                        "Main article: Spring Security\r\n" + //
                        "Spring Boot has integration with the Spring Security Module. The simplest way for integrating Spring Boot with Spring Security is to declare the starter dependency in the build configuration file.[20]\r\n" + //
                        "\r\n" + //
                        "If Maven is used as the build tool, then the dependency with artifact ID spring-boot-starter-security dependency can be specified in the pom.xml configuration file.[20]\r\n" + //
                        "\r\n" + //
                        "Application servers\r\n" + //
                        "By default, Spring boot provides embedded web servers (such as Tomcat) out-of-the-box.[21] However, Spring Boot can also be deployed as a WAR file on a standalone WildFly application server.[22]\r\n" + //
                        "\r\n" + //
                        "If Maven is used as the build tool, there is a wildfly-maven-plugin Maven plugin that allows for automatic deployment of the generated WAR file.[22]");
        post01.setAccount(account01);
        postService.save(post01);

        Post post02=new Post();
        post02.setTitle("Data Structures");
        post02.setBody("In computer science, a data structure is a data organization and storage format that is usually chosen for efficient access to data.[1][2][3] More precisely, a data structure is a collection of data values, the relationships among them, and the functions or operations that can be applied to the data,[4] i.e., it is an algebraic structure about data.\r\n" + //
                        "\r\n" + //
                        "Usage\r\n" + //
                        "Data structures serve as the basis for abstract data types (ADT). The ADT defines the logical form of the data type. The data structure implements the physical form of the data type.[5]\r\n" + //
                        "\r\n" + //
                        "Different types of data structures are suited to different kinds of applications, and some are highly specialized to specific tasks. For example, relational databases commonly use B-tree indexes for data retrieval,[6] while compiler implementations usually use hash tables to look up identifiers.[7]\r\n" + //
                        "\r\n" + //
                        "Data structures provide a means to manage large amounts of data efficiently for uses such as large databases and internet indexing services. Usually, efficient data structures are key to designing efficient algorithms. Some formal design methods and programming languages emphasize data structures, rather than algorithms, as the key organizing factor in software design. Data structures can be used to organize the storage and retrieval of information stored in both main memory and secondary memory.[8]\r\n" + //
                        "\r\n" + //
                        "Implementation\r\n" + //
                        "Data structures can be implemented using a variety of programming languages and techniques, but they all share the common goal of efficiently organizing and storing data.[9] Data structures are generally based on the ability of a computer to fetch and store data at any place in its memory, specified by a pointer—a bit string, representing a memory address, that can be itself stored in memory and manipulated by the program. Thus, the array and record data structures are based on computing the addresses of data items with arithmetic operations, while the linked data structures are based on storing addresses of data items within the structure itself. This approach to data structuring has profound implications for the efficiency and scalability of algorithms. For instance, the contiguous memory allocation in arrays facilitates rapid access and modification operations, leading to optimized performance in sequential data processing scenarios.[10]\r\n" + //
                        "\r\n" + //
                        "The implementation of a data structure usually requires writing a set of procedures that create and manipulate instances of that structure. The efficiency of a data structure cannot be analyzed separately from those operations. This observation motivates the theoretical concept of an abstract data type, a data structure that is defined indirectly by the operations that may be performed on it, and the mathematical properties of those operations (including their space and time cost).[11]\r\n" + //
                        "\r\n" + //
                        "A landmark 1989 study showed how ordinary pointer-based structures can be transformed into persistent data structures – versions that preserve and share earlier states after updates without asymptotically increasing time or space costs.[12]\r\n" + //
                        "\r\n" + //
                        "Examples\r\n" + //
                        "Main article: List of data structures\r\n" + //
                        "\r\n" + //
                        "The standard type hierarchy of the programming language Python 3.\r\n" + //
                        "There are numerous types of data structures, generally built upon simpler primitive data types. Well known examples are:[13]\r\n" + //
                        "\r\n" + //
                        "An array is a number of elements in a specific order, typically all of the same type (depending on the language, individual elements may either all be forced to be the same type, or may be of almost any type). Elements are accessed using an integer index to specify which element is required. Typical implementations allocate contiguous memory words for the elements of arrays (but this is not always a necessity). Arrays may be fixed-length or resizable.\r\n" + //
                        "A linked list (also just called list) is a linear collection of data elements of any type, called nodes, where each node has itself a value, and points to the next node in the linked list. The principal advantage of a linked list over an array is that values can always be efficiently inserted and removed without relocating the rest of the list. Certain other operations, such as random access to a certain element, are however slower on lists than on arrays.\r\n" + //
                        "A record (also called tuple or struct) is an aggregate data structure. A record is a value that contains other values, typically in fixed number and sequence and typically indexed by names. The elements of records are usually called fields or members. In the context of object-oriented programming, records are known as plain old data structures to distinguish them from objects.[14]\r\n" + //
                        "Hash tables, also known as hash maps, are data structures that provide fast retrieval of values based on keys. They use a hashing function to map keys to indexes in an array, allowing for constant-time access in the average case. Hash tables are commonly used in dictionaries, caches, and database indexing. However, hash collisions can occur, which can impact their performance. Techniques like chaining and open addressing are employed to handle collisions.\r\n" + //
                        "Graphs are collections of nodes connected by edges, representing relationships between entities. Graphs can be used to model social networks, computer networks, and transportation networks, among other things. They consist of vertices (nodes) and edges (connections between nodes). Graphs can be directed or undirected, and they can have cycles or be acyclic. Graph traversal algorithms include breadth-first search and depth-first search.\r\n" + //
                        "Stacks and queues are abstract data types that can be implemented using arrays or linked lists. A stack has two primary operations: push (adds an element to the top of the stack) and pop (removes the topmost element from the stack), that follow the Last In, First Out (LIFO) principle. Queues have two main operations: enqueue (adds an element to the rear of the queue) and dequeue (removes an element from the front of the queue) that follow the First In, First Out (FIFO) principle.\r\n" + //
                        "Trees represent a hierarchical organization of elements. A tree consists of nodes connected by edges, with one node being the root and all other nodes forming subtrees. Trees are widely used in various algorithms and data storage scenarios. Binary trees (particularly heaps), AVL trees, and B-trees are some popular types of trees. They enable efficient and optimal searching, sorting, and hierarchical representation of data.\r\n" + //
                        "A trie, or prefix tree, is a special type of tree used to efficiently retrieve strings. In a trie, each node represents a character of a string, and the edges between nodes represent the characters that connect them. This structure is especially useful for tasks like autocomplete, spell-checking, and creating dictionaries. Tries allow for quick searches and operations based on string prefixes.\r\n" + //
                        "\r\n" + //
                        "Language support\r\n" + //
                        "Most assembly languages and some low-level languages, such as BCPL (Basic Combined Programming Language), lack built-in support for data structures. On the other hand, many high-level programming languages and some higher-level assembly languages, such as MASM, have special syntax or other built-in support for certain data structures, such as records and arrays. For example, the C (a direct descendant of BCPL) and Pascal languages support structs and records, respectively, in addition to vectors (one-dimensional arrays) and multi-dimensional arrays.[15][16]\r\n" + //
                        "\r\n" + //
                        "Most programming languages feature some sort of library mechanism that allows data structure implementations to be reused by different programs. Modern languages usually come with standard libraries that implement the most common data structures. Examples are the C++ Standard Template Library, the Java Collections Framework, and the Microsoft .NET Framework.\r\n" + //
                        "\r\n" + //
                        "Modern languages also generally support modular programming, the separation between the interface of a library module and its implementation. Some provide opaque data types that allow clients to hide implementation details. Object-oriented programming languages, such as C++, Java, and Smalltalk, typically use classes for this purpose.\r\n" + //
                        "\r\n" + //
                        "Many known data structures have concurrent versions which allow multiple computing threads to access a single concrete instance of a data structure simultaneously.[17]");
        post02.setAccount(account02);
        postService.save(post02);
        //we are not adding createdAt because in service layer when record is new we are automatically adding current date and time
       }
    }
    
}
