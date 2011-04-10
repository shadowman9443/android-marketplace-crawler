class CreateVisuals < ActiveRecord::Migration
  def self.up
    create_table :visuals do |t|
      t.references :app
      
      t.timestamps
    end
    add_index :visuals, :app_id
  end

  def self.down
    drop_table :visuals
  end
end
