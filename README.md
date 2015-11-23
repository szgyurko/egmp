EGMP
====

What's EGMP?

    EGMP stands for Extensible Group Management Protocol. It's purpose is to coordinate clustered applications with
    a basic elevation scheme between the nodes. The nodes are periodically exchanging statuses and based on the selected
    elevation strategy one of them becomes elevated.

    This status later can be queried by the user. The objective is to identify a single master node within a cluster and
    therefore allow applications to run tasks on a single node.

What is the purpose?

    Typical example of usage is when the user have an application which runs certain maintenance task periodically. If
    the same application is deployed across multiple nodes, the question is which node should run these tasks as it's
    unlikely that all of them can run the task. One way would be to manually fix it in a config which node can run
    these but in case of a node failure none of them will ever run the tasks. EGMP meant to coordinate such situation.

What are the supported elevation and communication methods?

    Currently EGMP can communicate over unicast or multicast both over IPv4 and IPv6. It includes a heartbeat sender
    and listener without further coding needed. In case the sender will be called manually by your application, the
    built-in scheduler may be disabled.

    In terms of elevation strategies, it supports static node ID based elevation and IP address based elevation. As the
    project's name suggest this is all extensible and you can easily implement your own elevation strategy without
    touching any of the core code.

Is IPv6 supported?

    Yes, IPv6 is fully supported and tested.

Is Spring supported?

    Yes, spring is fully supported and a spring config example is provided.

Can I commit fixes?

    You're more than welcome to contribute, I'm happy to receive pull requests.

License

    The project is licensed under GPLv2 strictly. No later versions of the license is allowed.
