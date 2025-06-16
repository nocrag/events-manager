package nbcc.springresto.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.repositories.EventRepository;
import nbcc.springresto.repositories.LayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;
    private final EventRepository eventRepository;

    public LayoutService(LayoutRepository layoutRepository, EventRepository eventRepository) {

        this.layoutRepository = layoutRepository;
        this.eventRepository = eventRepository;
    }

    public List<Layout> findAll() {
        return layoutRepository.findActiveLayoutsByIsArchivedFalse();
    }

    //catch null here instead of controller/this is an option
    public Layout findById(Long id) {
        return layoutRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Layout not found"));
    }

    public void delete(long id) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found with ID: " + id));

        boolean isUsedByEvents = eventRepository.existsByLayout(layout);

        if (isUsedByEvents) {
            layout.setArchived(true);
            layoutRepository.save(layout);
        } else {
            layoutRepository.delete(layout);
        }
    }


    public void create(Layout layout) {
        layoutRepository.save(layout);
    }

    public Layout update(long id, Layout layout) {
        var dbOptionalLayout = layoutRepository.findById(id);

        if (dbOptionalLayout.isEmpty()) {
            throw new EntityNotFoundException("Layout not found with id " + id);
        }
        var dbLayout = dbOptionalLayout.get();

        dbLayout.setName(layout.getName());
        dbLayout.setDescription(layout.getDescription());
        dbLayout.setDiningTable(layout.getDiningTables());

        return layoutRepository.save(dbLayout);
    }

    public List<Layout> findAllActive() {
        return layoutRepository.findActiveLayoutsByIsArchivedFalse();
    }

    public List<Layout> findAllArchived() {
        return layoutRepository.findByIsArchivedTrue();
    }


}
