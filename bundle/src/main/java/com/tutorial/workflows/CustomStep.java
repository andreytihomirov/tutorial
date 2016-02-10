package com.tutorial.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by andrey.tihomirov on 09.02.2016.
 * This Service represent step of workflow to tagging uploaded images to DAM.
 */
@Service
@Component(label = "Step of workflow to tagging uploaded images to DAM", immediate = true, metatype = true)
public class CustomStep implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(CustomStep.class);

    public static final String EMPTY_STRING = "";
    public static final String SPLIT_BY_COMMA_EXPRESSION = "\\,\\s";
    private static final String TYPE_JCR_PATH = "JCR_PATH";

    private static final String [] ANIMAL_TAG =  {
            "stockphotography:animals"
    };

    @Property(description = "Key words")
    private static final String KEY_WORDS = "key.words";

    @Property(description = "tag name")
    private static final String TAG_NAME = "tag.name";

    private String[] keyWords;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;
    private TagManager tagManager;
    private Tag tag;
    private Asset asset;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        log.error("execute() called...");

        WorkflowData workflowData = workItem.getWorkflowData();

        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {

            final String payloadPath = workItem.getWorkflowData().getPayload().toString();
            asset = DamUtil.resolveToAsset(resourceResolver.getResource(payloadPath));
            String nodeName = asset.getName();

            for (String key : keyWords) {
                if (nodeName.toLowerCase().contains(key.toLowerCase())) {
                    Resource resource = resourceResolver.getResource(asset.getPath() + "/jcr:content/metadata");
                    Tag[] tagsArr = tagManager.getTags(resource);
                    List<Tag> tags = new ArrayList<Tag>(Arrays.asList(tagsArr));
                    tags.add(tag);
                    tagManager.setTags(resource, tags.toArray(tagsArr));
                }
            }
        }
    }

    @Activate
    @Modified
    protected final void activate(final Map<String, Object> properties) {

        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        tagManager = resourceResolver.adaptTo(TagManager.class);

        String rawKeyWords = properties.get(KEY_WORDS).toString();
        if(!rawKeyWords.equalsIgnoreCase(EMPTY_STRING)) {
            keyWords = rawKeyWords.split(SPLIT_BY_COMMA_EXPRESSION);
        }

        String tagName = properties.get(TAG_NAME).toString();
        tag = tagManager.resolve(tagName);

    }

}