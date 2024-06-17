package framework;

import java.io.IOException;

import domain.group.Group;
import domain.group.GroupDomainService;

public class GroupFileService extends GroupDomainService {
    private final GroupFileRepository fileRepository;

    public GroupFileService(final GroupFileRepository repository) {
        super(repository);
        this.fileRepository = repository;
    }

    @Override
    public void add(final Group group) throws IOException {
        super.add(group);
        fileRepository.commit();
    }

    @Override
    public void update(final Group group) throws IOException {
        super.update(group);
        fileRepository.commit();
    }

    @Override
    public void remove(final String name) throws IOException {
        super.remove(name);
        fileRepository.commit();
    }
}
