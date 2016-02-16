Ejst.CustomWidget = CQ.Ext.extend(CQ.form.CompositeField, {
 
    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    hiddenField: null,
 
    /**
     * @private
     * @type CQ.Ext.form.ComboBox
     */
    allowField: null,
 
    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    otherField: null,
     
    constructor: function(config) {
        config = config || { };
        var defaults = {
            "border": false,
            "layout": "table",
            "columns":2
        };
        config = CQ.Util.applyDefaults(config, defaults);
        Ejst.CustomWidget.superclass.constructor.call(this, config);
    },
 
    // overriding CQ.Ext.Component#initComponent
    initComponent: function() {
        Ejst.CustomWidget.superclass.initComponent.call(this);
 
        this.hiddenField = new CQ.Ext.form.Hidden({
            name: this.name
        });
        this.add(this.hiddenField);
 
        this.allowField = new CQ.form.Selection({
            type:"select",
            cls:"ejst-customwidget-1",
            listeners: {
                selectionchanged: {
                    scope:this,
                    fn: this.updateHidden
                }
            },
            optionsProvider: this.optionsProvider
        });
        this.add(this.allowField);
 
        this.otherField = new CQ.Ext.form.TextField({
            cls:"ejst-customwidget-2",
            listeners: {
                change: {
                    scope:this,
                    fn:this.updateHidden
                }
            }
        });
        this.add(this.otherField);
 
    },
 
    // overriding CQ.form.CompositeField#processPath
    processPath: function(path) {
        console.log("CustomWidget#processPath", path);
        this.allowField.processPath(path);
    },
 
    // overriding CQ.form.CompositeField#processRecord
    processRecord: function(record, path) {
        console.log("CustomWidget#processRecord", path, record);
        this.allowField.processRecord(record, path);
    },
 
    // overriding CQ.form.CompositeField#setValue
    setValue: function(value) {
        var parts = value.split("/");
        this.allowField.setValue(parts[0]);
        this.otherField.setValue(parts[1]);
        this.hiddenField.setValue(value);
    },
 
    // overriding CQ.form.CompositeField#getValue
    getValue: function() {
        return this.getRawValue();
    },
 
    // overriding CQ.form.CompositeField#getRawValue
    getRawValue: function() {
        if (!this.allowField) {
            return null;
        }
        return this.allowField.getValue() + "/" +
               this.otherField.getValue();
    },
 
    // private
    updateHidden: function() {
        this.hiddenField.setValue(this.getValue());
    }
 
});
 
// register xtype
CQ.Ext.reg('ejstcustom', Ejst.CustomWidget);