package framework;

import java.io.IOException;

import domain.group.GroupDomainService;

public class GroupFileService extends GroupDomainService {
    private final GroupFileRepository fileRepository;

    public GroupFileService(final GroupFileRepository repository) {
        super(repository);
        this.fileRepository = repository;
    }

    @Override
    public void add(final String name, final Integer frequencyInDays) throws IOException {
        super.add(name, frequencyInDays);
        fileRepository.commit();
    }

    @Override
    public void add(final String name) throws IOException {
        super.add(name);
        fileRepository.commit();
    }

    @Override
    public void update(final String name, final int frequencyInDays) throws IOException {
        super.update(name, frequencyInDays);
        fileRepository.commit();
    }

    @Override
    public void remove(final String name) throws IOException {
        super.remove(name);
        fileRepository.commit();
    }
}
