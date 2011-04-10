class CreateComments < ActiveRecord::Migration
  def self.up
    create_table :comments do |t|
      t.integer :rating
      t.string :creationTime
      t.string :authorName
      t.string :text
      t.string :authorId
      t.references :app

      t.timestamps
    end
    add_index :comments, :app_id
    add_index :comments, :authorId    
  end

  def self.down
    drop_table :comments
  end
end
