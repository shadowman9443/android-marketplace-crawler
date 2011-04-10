class CommentsController < ApplicationController

  # POST /comments.xml
  # POST /comments.json
  def create    
    @app = App.find(params[:app_id])
    @comment = @app.comments.new(params[:comment])    
    
    respond_to do |format|
      if @comment.save
        format.xml  { render :xml => @comment, :status => :created, :location => @comment }
        format.json  { render :json => @comment, :status => :created, :location => @comment }        
      else
        format.xml  { render :xml => @comment.errors, :status => :unprocessable_entity }
        format.json  { render :json => @comment.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # PUT /comments/1.xml
  # PUT /comments/1.json  
  def update
    @comment = Comment.find(params[:id])

    respond_to do |format|
      if @comment.update_attributes(params[:comment])
        format.xml  { head :ok }
        format.json  { head :ok }        
      else
        format.xml  { render :xml => @comment.errors, :status => :unprocessable_entity }
        format.json  { render :json => @comment.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # DELETE /comments/1.xml
  # DELETE /comments/1.json  
  def destroy
    @comment = Comment.find(params[:id])
    @comment.destroy
    
    respond_to do |format|
      format.xml  { head :ok }
      format.json  { head :ok }      
    end
  end
  
  # POST /comments/exists.xml
  # POST /comments/exists.json
  def exists
    @record = Record.new
    comment = Comment.find_by_app_id_and_authorId(params[:app_id], params[:authorId], :select => :id)

    if comment.nil?
      @record.exists = false
    else
      @record.id = comment.id
      @record.exists = true
    end

    respond_to do |format|
      format.xml {render :xml => @record}
      format.json {render :json => @record}      
    end
  end
end
