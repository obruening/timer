package obruening.timer.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javafx.application.Platform;
import obruening.timer.model.primary.Comment;
import obruening.timer.model.primary.Post;
import obruening.timer.model.primary.Rating;
import obruening.timer.model.primary.berechtigung.Antragsteller;
import obruening.timer.model.primary.berechtigung.Berechtigung;
import obruening.timer.model.primary.berechtigung.Profil;
import obruening.timer.service.primary.PostService;
import obruening.timer.service.primary.berechtigung.BerechtigungService;
import obruening.timer.store.event.AutoUpdateEvent;
import obruening.timer.store.event.CreatePostEvent;
import obruening.timer.store.event.NotifyEvent;
import obruening.timer.store.event.RefreshEvent;
import obruening.timer.store.event.SelectPostEvent;
import obruening.timer.store.event.TimerTriggeredEvent;
import obruening.timer.store.event.workflow.BerechtigungenEvent;
import obruening.timer.store.event.workflow.CallActivityEvent;
import obruening.timer.store.event.workflow.CancelAndContinueEvent;
import obruening.timer.store.event.workflow.MultiTimerEvent;
import obruening.timer.store.event.workflow.TimerBoundaryEvent;
import obruening.timer.store.event.workflow.TimerIntermediateEvent;
import obruening.timer.store.event.workflow.processinstance.DeleteProcessInstanceEvent;
import obruening.timer.store.event.workflow.processinstance.HistoricProcessInstanceSelectedEvent;
import obruening.timer.store.event.workflow.processinstance.TerminateAllProcessInstancesEvent;
import obruening.timer.store.event.workflow.task.CompleteTaskEvent;
import obruening.timer.store.event.workflow.task.TaskSelectedEvent;

@Component
public class Store {

    private static Logger logger = LoggerFactory.getLogger(Store.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private PostService postService;

    @Autowired
    private BerechtigungService berechtigungService;
    
    private List<Task> taskList = new ArrayList<>();
    private List<HistoricProcessInstance> historicProcessInstanceList = new ArrayList<>();
    private Task selectedTask = null;
    private HistoricProcessInstance selectedHistoricProcessInstance = null;
    private String taskMessage = "Select task.";
    private String historicProcessInstanceMessage = "Select process instance.";

	private Timer timer;

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<HistoricProcessInstance> getHistoricProcessInstanceList() {
        return historicProcessInstanceList;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public String getTaskMessage() {
        return taskMessage;
    }
    
    public HistoricProcessInstance getSelectedHistoricProcessInstance() {
		return selectedHistoricProcessInstance;
	}
    
    public String getHistoricProcessInstanceMessage() {
		return historicProcessInstanceMessage;
	}

    private void notifyListeners() {
        applicationEventPublisher.publishEvent(new NotifyEvent());
    }

    @EventListener
    private void onTimerIntermediateEvent(TimerIntermediateEvent timerIntermediateEvent) {
        runtimeService.startProcessInstanceByKey("timer_intermediate_process");
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onTimerBoundaryEvent(TimerBoundaryEvent timerBoundaryEvent) {
        runtimeService.startProcessInstanceByKey("timer_boundary_process");
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onCallActivityEvent(CallActivityEvent callerEvent) {
    	Map<String, Object> map = new TreeMap<>();
    	map.put("var1", 111L);
        runtimeService.startProcessInstanceByKey("call_activity_process", map);
        reloadTaskAndProcessInstances();
        notifyListeners();
    }

    @EventListener
    private void onCancelAndContinueEvent(CancelAndContinueEvent cancelAndContinueEvent) {
        runtimeService.startProcessInstanceByKey("cancel_and_continue_process");
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onBerechtigungenEvent(BerechtigungenEvent berechtigungenEvent) {
        
        Berechtigung berechtigung = new Berechtigung();
        
        List<Antragsteller> antragstellerList = new ArrayList<>();
        
        Antragsteller antragsteller1 = new Antragsteller();
        antragsteller1.setUserId("A001");
        antragsteller1.setVerantwortlicher("VA001");
        antragsteller1.setBerechtigung(berechtigung);
        antragsteller1.setGenehmigt(true);
        antragstellerList.add(antragsteller1);

        Antragsteller antragsteller2 = new Antragsteller();
        antragsteller2.setUserId("A002");
        antragsteller2.setVerantwortlicher("VA002");
        antragsteller2.setBerechtigung(berechtigung);
        antragsteller2.setGenehmigt(true);
        antragstellerList.add(antragsteller2);
        
        berechtigung.setAntragstellerList(antragstellerList);
        
        
        List<Profil> profilList = new ArrayList<>();
        
        Profil profil1 = new Profil();
        profil1.setName("sam");
        profil1.setVerantwortlicher("VP001");
        profil1.setBerechtigung(berechtigung);
        profilList.add(profil1);

        Profil profil2 = new Profil();
        profil2.setName("spf");
        profil2.setVerantwortlicher("VP002");
        profil2.setBerechtigung(berechtigung);
        profilList.add(profil2);
        
        berechtigung.setProfilList(profilList);
        berechtigung.setGenehmigt(true);

        
        berechtigung = berechtigungService.save(berechtigung);
        
        Map<String, Object> map = new TreeMap<>();
        map.put("berechtigung", berechtigung);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("berechtigungen_process", map);
        
        berechtigung.setProcessInstanceId(processInstance.getId());
        
        berechtigungService.save(berechtigung);
        
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onMultiTimerEvent(MultiTimerEvent multiTimerEvent) {
        runtimeService.startProcessInstanceByKey("multi_timer_process");
        reloadTaskAndProcessInstances();
        notifyListeners();
    }

    @EventListener
    private void onTaskSelectedEvent(TaskSelectedEvent taskSelectedEvent) {
        this.selectedTask = taskSelectedEvent.getTask();
        this.taskMessage = String.format("Selected task: %s", selectedTask.getId());
        notifyListeners();
    }
    
    @EventListener
    private void onHistoricProcessInstanceSelectedEvent(HistoricProcessInstanceSelectedEvent historicProcessInstanceSelectedEvent) {
        this.selectedHistoricProcessInstance = historicProcessInstanceSelectedEvent.getHistoricProcessInstance();
        this.historicProcessInstanceMessage = String.format("Selected process instance: %s", selectedHistoricProcessInstance.getId());
        notifyListeners();
    }


    @EventListener
    private void onCompleteTaskEvent(CompleteTaskEvent completeTaskEvent) {
        taskService.complete(completeTaskEvent.getTask().getId());
        this.selectedTask = null;
        this.taskMessage = String.format("Task %s completed.", completeTaskEvent.getTask().getId());
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onDeleteProcessInstanceEvent(DeleteProcessInstanceEvent deleteProcessInstanceEvent) {
    	runtimeService.deleteProcessInstance(deleteProcessInstanceEvent.getHistoricProcessInstance().getId(), "terminated by user.");
        this.selectedHistoricProcessInstance = null;
        this.historicProcessInstanceMessage = String.format("Process instance %s deleted.", deleteProcessInstanceEvent.getHistoricProcessInstance().getId());
        reloadTaskAndProcessInstances();
        notifyListeners();
    }

    @EventListener
    private void onTimerTriggeredEvent(TimerTriggeredEvent timerTriggeredEvent) {
        reloadTaskAndProcessInstances();
        notifyListeners();
        logger.info("--------------" + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @EventListener
    private void onRefreshEvent(RefreshEvent refreshEvent) {
        reloadTaskAndProcessInstances();
        notifyListeners();
    }

    @EventListener
    private void onTerminateAllProcessInstancesEvent(TerminateAllProcessInstancesEvent terminateEvent) {
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().active().list();
        processInstanceList.stream().forEach(p -> runtimeService.deleteProcessInstance(p.getId(), "Deleted by user."));
        reloadTaskAndProcessInstances();
        notifyListeners();
    }
    
    @EventListener
    private void onCreatePostEvent(CreatePostEvent createPostEvent) {
    	Post post = new Post();
    	post.setText("bla");
    	List<Comment> commentList = new ArrayList<>();
    	Comment comment1 = new Comment();
    	comment1.setPost(post);
    	comment1.setText("text1");
    	
    	List<Rating> ratingList = new ArrayList<>();
    	Rating rating1 = new Rating();
    	rating1.setStar(11L);
    	rating1.setComment(comment1);
    	ratingList.add(rating1);
    	comment1.setRatings(ratingList);

    	
    	
    	commentList.add(comment1);
    	
    	Comment comment2 = new Comment();
    	comment2.setPost(post);
    	comment2.setText("text2");
    	commentList.add(comment2);
    	
    	post.setComments(commentList);
    	
    	postService.save(post);

    }
    
    @EventListener
    private void onSelectPostEvent(SelectPostEvent selectPostEvent) {
    	List<Post> postList = postService.findAll();
    	Post post = postList.get(0);
    	List<Comment> commentList = post.getComments();
    	commentList.remove(commentList.get(0));
    	
    	post.setText("xxxxxx");
    	postService.save(post);
    	
    }

    @EventListener
    private void onAutoUpdateEvent(AutoUpdateEvent autoUpdateEvent) {

        if (autoUpdateEvent.isEnabled()) {

            if (timer != null) {
                timer.cancel();
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    logger.info("----- in run");

                    reloadTaskAndProcessInstances();

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            logger.info("----- in runlater");
                            notifyListeners();
                        }
                    });
                }
            }, 0, 5000);
        } else {

            if (timer != null) {
                timer.cancel();
                logger.info("----- cancelled");
            }
        }
    }

    // @Transactional("primaryTransactionManager")
    private void reloadTaskAndProcessInstances() {
        taskList = taskService
        		.createTaskQuery()
        		.orderByTaskCreateTime()
        		.asc()
        		.list();
        
        selectedTask = getSelectedTaskInList(selectedTask, taskList);
        if (selectedTask == null) {
        	taskMessage = "Select task.";
        }
        
        historicProcessInstanceList = historyService
        		.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime()
                .asc()
                .list();
        
        if (!isSelectedHistoricProcessInstancekInList(selectedHistoricProcessInstance, historicProcessInstanceList)) {
        	selectedHistoricProcessInstance = null;
        }
    }
    
    private Task getSelectedTaskInList(Task task, List<Task> taskList) {
    	
    	if (task == null) {
    		return null;
    	}
    	
    	Optional<Task> item = taskList.stream().filter((h) -> h.getId().equals(task.getId())).findFirst();
    	if (item.isPresent() ) {
    		return item.get();
    	} else {
    		return null;
    	}
    }
    
    
    private boolean isSelectedHistoricProcessInstancekInList(HistoricProcessInstance historicProcessInstance, List<HistoricProcessInstance> historicProcessInstanceList) {
    	
    	if (historicProcessInstance == null) {
    		return false;
    	}
    	
    	Optional<HistoricProcessInstance> item = historicProcessInstanceList.stream().filter((h) -> h.getId().equals(historicProcessInstance.getId())).findFirst();
    	return item.isPresent();
    }

}
