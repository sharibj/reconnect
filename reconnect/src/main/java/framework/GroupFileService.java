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
    public void addGroup(final String name, final Integer frequencyInDays) throws IOException {
        super.addGroup(name, frequencyInDays);
        fileRepository.commit();
    }

    @Override
    public void addGroup(final String name) throws IOException {
        super.addGroup(name);
        fileRepository.commit();
    }

    @Override
    public void updateByName(final String name, final int frequencyInDays) throws IOException {
        super.updateByName(name, frequencyInDays);
        fileRepository.commit();
    }

    @Override
    public void updateById(final String id, final String name, final int frequencyInDays) throws IOException {
        super.updateById(id, name, frequencyInDays);
        fileRepository.commit();
    }

    @Override
    public void removeGroupById(final String id) throws IOException {
        super.removeGroupById(id);
        fileRepository.commit();
    }

    @Override
    public void removeGroupByName(final String name) throws IOException {
        super.removeGroupByName(name);
        fileRepository.commit();
    }
}
