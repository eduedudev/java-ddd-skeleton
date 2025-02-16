package com.devsoftec.jaap.users.shared.infrastructure.cli;

import com.devsoftec.jaap.users.shared.domain.Service;
import com.devsoftec.jaap.users.shared.infrastructure.bus.event.mariadb.MariaDBDomainEventsConsumer;

@Service
public final class ConsumeMariaDBDomainEventsCommand extends ConsoleCommand {
    private final MariaDBDomainEventsConsumer consumer;

    public ConsumeMariaDBDomainEventsCommand(MariaDBDomainEventsConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void execute(String[] args){
        try {
            while (true){
                consumer.consume();
                Thread.sleep(5000);
            }
        }catch (Exception e){
            error(e.getMessage());
        }
    }
}
